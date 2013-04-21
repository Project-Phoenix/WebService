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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.HttpHeaders;

import de.phoenix.PhoenixApplication;

/**
 * Manages the token by holding them in maps and generating new ones
 * 
 */
public class TokenManager {

    // Maps for fast access to tokens
    private Map<String, Token> tokenmapByID;

    // Time for token before it exeeds
    private final static long TOKEN_DURATION = TimeUnit.HOURS.toMillis(1);

    public TokenManager() {
        tokenmapByID = new HashMap<String, Token>();
    }

    /**
     * Generates a token for the user by using {@link UUID#randomUUID()}
     * 
     * @param owner
     *            The owner of the token. Every owner can have one token per
     *            time!
     * @return A valid token
     */
    public Token generateToken(String owner) {
        // Generate a unique, but also random order of chars hard to guess
        UUID tokenID = UUID.randomUUID();

        // Generate a new token based on its unique id
        Token token = new Token(tokenID.toString(), owner, TOKEN_DURATION);

        // save temponary
        tokenmapByID.put(token.getID(), token);

        PhoenixApplication.accountManager.getUser(owner).setToken(token);
        return token;
    }

    /**
     * Checks if the token attached to every webresource call is valid by
     * checking the existence of the ID and then if the token is maybe expired.
     * Expired tokens or not existing tokes are invalid and denied
     * 
     * @param headers
     *            The headers directly from the http request
     * @return True, when the token is valid and not expired
     */
    public boolean isValidToken(HttpHeaders headers) {
        return isValidToken(extractTokenID(headers));
    }

    public boolean isValidToken(String tokenID) {
        if (tokenID == null)
            return false;
        return isValidToken(tokenmapByID.get(tokenID));
    }

    public boolean isValidToken(Token token) {
        return token != null && !token.isExpired();
    }

    public String extractTokenID(HttpHeaders headers) {
        return headers.getRequestHeaders().getFirst(TokenFilter.TOKEN_HEAD);
    }

    public Token getToken(HttpHeaders headers) {
        return getToken(extractTokenID(headers));
    }

    public Token getToken(String tokenID) {
        return tokenmapByID.get(tokenID);
    }

}
