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
 * A role with with some restricted permissions. Usefull, when a role should
 * have access to everything but not to some sub nodes. Grant the role the
 * permission "node.*" and disallow subnodes like "node.node1". The role have
 * access to everything below "node" but not to "node.node1"
 * 
 * @author Meldanor
 * 
 */
public class LimitedRole extends Role {

    /**
     * Holding all permission the role is not allowed to use
     */
    private PermissionTree blacklistPermissions;

    /**
     * Creates a role with a unique name and empty whitelist and blacklist
     * 
     * @param name
     *            The unique name of the role
     */
    public LimitedRole(String name) {
        super(name);
        this.blacklistPermissions = new PermissionTree();
    }

    /**
     * Creates a role with a unique name and set the permission the role is
     * allowed to (whitelist) and the permissions the role isn't (blacklist)
     * 
     * @param name
     *            The unique name of the role
     * @param whiteList
     *            The role can use this permissions
     * @param blackList
     *            The role cannot use them ignoring the whitelist
     */
    public LimitedRole(String name, PermissionTree whiteList, PermissionTree blackList) {
        super(name, whiteList);
        this.blacklistPermissions = blackList;
    }

    /**
     * Inheritates the permission from the parent role. The parent role
     * permission are untouched and this role is a deep copy! Everything happens
     * to the parent hole do not affects this child role
     * 
     * @param name
     *            The unique name of the role
     * @param parentRole
     *            The role this role inheritantes all permissions. Can be a
     *            limited role to copy also the restricted permissions
     */
    public LimitedRole(String name, Role parentRole) {
        super(name, parentRole);
        if (parentRole instanceof LimitedRole)
            this.blacklistPermissions = ((LimitedRole) parentRole).blacklistPermissions.copy();
        else
            this.blacklistPermissions = new PermissionTree();
    }

    /**
     * Inheritates the permission from the parent role. The parent role
     * permission are untouched and this role is a deep copy! Everything happens
     * to the parent hole do not affects this child role
     * 
     * @param name
     *            The unique name of the role
     * @param parentRole
     *            The role this role inheritantes all permissions. Can be a
     *            limited role to copy also the restricted permissions
     * @param additionalWhiteList
     *            The role add this permission to its inheritated whitelist
     * @param additionalBlackList
     *            The role add this permission to its inheritated blacklist
     */
    public LimitedRole(String name, Role parentRole, PermissionTree additionalWhiteList, PermissionTree additionalBlackList) {
        super(name, parentRole, additionalWhiteList);
        if (parentRole instanceof LimitedRole)
            this.blacklistPermissions = additionalBlackList.copyAndAdd(((LimitedRole) parentRole).blacklistPermissions);
        else
            this.blacklistPermissions = new PermissionTree();
    }

    /**
     * A disallowed permission overrides the granted permission. This does not
     * changing the whitelist!
     * 
     * @param node
     *            The permission node. Must be in the format "parent.child" or
     *            "parent.*"!
     */
    public void disallowPermission(String node) {
        this.blacklistPermissions.addNode(node);
    }

    /**
     * Removes the permission from the blacklist. This does not changing the
     * whitelist!
     * 
     * @param node
     *            The permission node. Must be in the format "parent.child" or
     *            "parent.*"!
     */
    public void reallowPermission(String node) {
        this.blacklistPermissions.removeNode(node);
    }

    /**
     * Checks if the permission node is on the blacklist. When the node is on
     * the blacklist,this role hasn't the permission, regardless if the
     * permission node is in the whitelist. If the permission node is not on the
     * blacklist, the role looks it up in the whitelist
     * 
     * @param permissionNode
     *            The permission node. Must be in the format "parent.child" or
     *            "parent.*"!
     * @return True when the role has direct access (parent.child) or wildcard
     *         for this node (parent.*)
     */
    @Override
    public boolean hasPermission(String permissionNode) {
        // Permission not
        return !blacklistPermissions.hasNode(permissionNode) && super.hasPermission(permissionNode);
    }
}
