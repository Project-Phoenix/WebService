/*
 * Copyright (C) 2014 Project-Phoenix
 * 
 * This file is part of WebService.
 * 
 * WebService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * WebService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WebService.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.phoenix.webresource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import de.phoenix.database.DatabaseManager;
import de.phoenix.database.entity.User;
import de.phoenix.database.entity.Userlevel;
import de.phoenix.database.entity.criteria.UserCriteriaFactory;
import de.phoenix.database.entity.criteria.UserLevelCriteriaFactory;
import de.phoenix.rs.PhoenixStatusType;
import de.phoenix.rs.key.KeyReader;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.security.Encrypter;
import de.phoenix.security.TokenManager;
import de.phoenix.security.SaltedPassword;
import de.phoenix.security.login.LoginAttempt;
import de.phoenix.security.user.CreatePhoenixUser;
import de.phoenix.security.user.PhoenixUser;
import de.phoenix.webresource.util.AbstractPhoenixResource;

@Path(PhoenixUser.WEB_RESOURCE_ROOT)
public class UserResource extends AbstractPhoenixResource<User, PhoenixUser> {

    public UserResource() {
        super(UserCriteriaFactory.getInstance());
    }

    @Path(PhoenixUser.WEB_RESOURCE_CREATE)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CreatePhoenixUser createPhoenixUser) {
        Session session = DatabaseManager.getSession();
        try {
            PhoenixUser pUser = (PhoenixUser) createPhoenixUser.getAttribute("user");
            if (isMailInUse(pUser, session)) {
                return Response.status(PhoenixStatusType.DUPLIATE_ENTITY).entity("Mail:" + pUser.getMail()).build();
            }
            if (isUsernameInUse(pUser, session)) {
                return Response.status(PhoenixStatusType.DUPLIATE_ENTITY).entity("Username:" + pUser.getUsername()).build();
            }

            Transaction trans = session.beginTransaction();

            SaltedPassword pw = Encrypter.getInstance().encryptPassword((String) createPhoenixUser.getAttribute("password"));

            User entity = new User(pUser, pw);
            Userlevel level = searchUnique(UserLevelCriteriaFactory.getInstance(), session, KeyReader.createSelect(pUser.getUserLevel()));
            entity.setUserlevelId(level);

            return handlePossibleDuplicateInsert(session, trans, entity);
        } finally {
            if (session != null)
                session.close();
        }
    }

    private boolean isMailInUse(PhoenixUser user, Session session) {

        String mail = user.getMail();
        return session.createCriteria(User.class).add(Restrictions.eq("mail", mail)).uniqueResult() != null;
    }

    private boolean isUsernameInUse(PhoenixUser user, Session session) {
        String username = user.getUsername();
        return session.createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult() != null;
    }

    @Path(PhoenixUser.WEB_RESOURCE_GET)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response get(SelectEntity<PhoenixUser> userSelector) {
        return onGet(userSelector);
    }

    private TokenManager tokenManager = TokenManager.INSTANCE;

    @Path(PhoenixUser.WEB_RESOURCE_LOGIN)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginAttempt loginAttempt) {

        Session session = DatabaseManager.getSession();
        try {
            User user = (User) UserCriteriaFactory.getInstance().extractCriteria(new SelectEntity<PhoenixUser>().addKey("username", loginAttempt.getUsername()), session).uniqueResult();
            if (user == null) {
                return Response.status(Status.UNAUTHORIZED).build();
            }
            String origHash = user.getHash();
            String toProveHash = loginAttempt.getPassword();

            String salt = user.getSalt();
            if (Encrypter.getInstance().validatePassword(toProveHash, origHash, salt)) {
                return Response.ok(tokenManager.createToken(user)).build();
            } else {
                return Response.status(Status.UNAUTHORIZED).build();
            }

        } finally {
            if (session != null)
                session.close();
        }
    }

}
