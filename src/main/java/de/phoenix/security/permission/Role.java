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

package de.phoenix.security.permission;

import de.phoenix.security.permission.datastructure.PermissionTree;

/**
 * A normal role with a permission tree. This role has no restricted permission
 * and have only the permission that has been granted
 * 
 * @author Meldanor
 * 
 */
public class Role {

    /**
     * The unique name of the role
     */
    private final String name;

    /**
     * The datastructure to hold the permission nodes
     */
    private PermissionTree permissions;

    /**
     * Creates a Role without any permission
     * 
     * @param name
     *            The name of the role
     */
    public Role(String name) {
        this(name, new PermissionTree());
    }

    /**
     * Create a Role with a given permission tree
     * 
     * @param name
     *            The name of the role
     * @param permissions
     *            The permission the Role is granted
     */
    public Role(String name, PermissionTree permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    /**
     * Inheritates every permissions from the parent tree. When the parent trees
     * permission changes this role is not touched!
     * 
     * @param name
     *            The name of the role
     * @param parentRole
     *            The role this role is coping the permission nodes
     */
    public Role(String name, Role parentRole) {
        this(name, new PermissionTree());
        this.permissions = parentRole.permissions.copy();
    }

    /**
     * Inheritates every permissions from the parent tree and adds additional
     * permission nodes. When the parent trees permission changes this role is
     * not touched!
     * 
     * @param name
     *            The name of the role
     * @param parentRole
     *            The role this role is coping the permission nodes
     * @param additionalPermission
     *            Additional permission this role is granted
     */
    public Role(String name, Role parentRole, PermissionTree additionalPermission) {
        this(name, parentRole);
        this.permissions = additionalPermission.copyAndAdd(permissions);
    }

    /**
     * @return The name of the role
     */
    public String getName() {
        return name;
    }

    /**
     * Has the role the permission node
     * 
     * @param permissionNode
     *            The permission node. Must be in the format "parent.child" or
     *            "parent.*"!
     * @return True when the role has direct access (parent.child) or wildcard
     *         for this node (parent.*)
     */
    public boolean hasPermission(String permissionNode) {
        return permissions.hasNode(permissionNode);
    }

    /**
     * Add a new permission node to the role the role is allowed to use
     * 
     * @param permissionNode
     *            The permission node. Must be in the format "parent.child" or
     *            "parent.*"!
     */
    public void grantPermission(String permissionNode) {
        permissions.addNode(permissionNode);
    }

    /**
     * Removes a permission node from the role. The role is not allowed for the
     * permission anymore
     * 
     * @param permissionNode
     *            The permission node. Must be in the format "parent.child" or
     *            "parent.*"!
     */
    public void revokePermission(String permissionNode) {
        permissions.removeNode(permissionNode);
    }

}
