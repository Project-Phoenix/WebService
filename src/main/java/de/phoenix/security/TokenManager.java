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

package de.phoenix.security;

import java.util.HashMap;
import java.util.Map;

import de.phoenix.database.entity.User;
import de.phoenix.security.login.PhoenixToken;

public class TokenManager {

    private Map<PhoenixToken, User> tokenMap;
    private Map<String, PhoenixToken> usernameTokenMap;

    private TokenManager() {
        this.tokenMap = new HashMap<PhoenixToken, User>();
        this.usernameTokenMap = new HashMap<String, PhoenixToken>();
    }

    public final static TokenManager INSTANCE = new TokenManager();

    public PhoenixToken createToken(User user) {
        PhoenixToken token = new PhoenixToken();

        // Delete old session if user tries to login twice
        if (usernameTokenMap.containsKey(user.getUsername())) {
            PhoenixToken tmp = usernameTokenMap.remove(user.getUsername());
            tokenMap.remove(tmp);
        }

        tokenMap.put(token, user);
        usernameTokenMap.put(user.getUsername(), token);
        return token;
    }

    public User getUserByToken(String token) {
        return getUserByToken(new PhoenixToken(token));
    }

    public User getUserByToken(PhoenixToken token) {
        return tokenMap.get(token);
    }

    public boolean isValid(PhoenixToken token) {
        return tokenMap.containsKey(token);
    }

}
