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

import de.phoenix.security.permission.Role;

public class User {

    /**
     * The unique name of the user
     */
    private final String name;
    /**
     * The role the user is belonging to
     */
    private Role role;

    /**
     * Token greated for this user
     */
    private Token token;

    /**
     * Use a hash calculated once per user to save performance. As long the
     * variables for the hash calculation are final this works!
     */
    private final int hash;

    /**
     * Create a user with an unique name to identify the user. The user does not
     * belong to any role and have so no permissions
     * 
     * @param name
     *            A unique name
     */
    public User(String name) {
        this(name, null);
    }

    /**
     * Create a user with an unique name to identify the user and put him into
     * the role
     * 
     * @param name
     *            A unique name
     * @param role
     *            The role defining the users permissions
     */
    public User(String name, Role role) {
        this.name = name;
        this.role = role;

        this.hash = calculateHash();
    }

    /**
     * @return The role the user belongs to
     */
    public Role getRole() {
        return role;
    }

    /**
     * Set the role the user belongs to
     * 
     * @param role
     *            The role the user shall belong to and have its permissions
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * @return The token belonging to this user
     */
    public Token getToken() {
        return token;
    }

    /**
     * Attach the token to the user - every user can only have one token at one
     * time
     * 
     * @param token
     *            The token which shall attach to the user
     */
    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * Remove the token from the user - the token will not destroyed or changed!
     */
    public void removeToken() {
        this.setToken(null);
    }

    /**
     * @return The unique name of the user
     */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * The hash of a user is calculated by the hash of the name * 31
     * 
     * @return The calculated string - it is done only once
     */
    private int calculateHash() {
        int t = this.name.hashCode();
        return (t << 4) - t;
    }

    /**
     * Two user are equals when the names are equal case insensitive
     * 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof User))
            return false;
        Role that = (Role) obj;

        return this.name.equalsIgnoreCase(that.getName());
    }

}
