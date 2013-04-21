/*
 * Copyright (C) 2013 Project-Phoenix
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

package de.phoenix.security;

import javax.ws.rs.core.HttpHeaders;

import de.phoenix.PhoenixApplication;

public class SecurityManager {

    /**
     * Checks if the token id in the http header is a valid token id and not
     * expired. Only a valid token id leads to an check if the user attached to
     * the token has the permissionnode
     * 
     * @param headers
     *            The header from the web call
     * @param permissionNode
     *            The permission node the user has to have to execute the method
     * @return <code>True</code> when the token id is valid and the user has the
     *         permission, <code>false</code> otherwise
     */
    public boolean isAllowed(HttpHeaders headers, String permissionNode) {

        Token token = PhoenixApplication.tokenManager.getToken(headers);
        if (!PhoenixApplication.tokenManager.isValidToken(token))
            return false;

        User user = PhoenixApplication.accountManager.getUser(token.getOwner());

        return user != null && user.getRole().hasPermission(permissionNode);
    }

}
