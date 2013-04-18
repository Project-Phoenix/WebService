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

public class LimitedRole extends Role {

    private PermissionTree blacklistPermissions;

    public LimitedRole(String name) {
        super(name);
        this.blacklistPermissions = new PermissionTree();
    }

    public LimitedRole(String name, PermissionTree whiteList, PermissionTree blackList) {
        super(name, whiteList);
        this.blacklistPermissions = blackList;
    }

    public LimitedRole(String name, Role parentRole) {
        super(name, parentRole);
        if (parentRole instanceof LimitedRole)
            this.blacklistPermissions = ((LimitedRole) parentRole).blacklistPermissions.copy();
        else
            this.blacklistPermissions = new PermissionTree();
    }

    public LimitedRole(String name, Role parentRole, PermissionTree additionalWhiteList, PermissionTree additionalBlackList) {
        super(name, parentRole, additionalWhiteList);
        if (parentRole instanceof LimitedRole)
            this.blacklistPermissions = additionalBlackList.copyAndAdd(((LimitedRole) parentRole).blacklistPermissions);
        else
            this.blacklistPermissions = new PermissionTree();
    }

    public void disallowPermission(String node) {
        this.blacklistPermissions.addNode(node);
    }

    public void reallowPermission(String node) {
        this.blacklistPermissions.removeNode(node);
    }

    @Override
    public boolean hasPermission(String permissionNode) {
        // Permission not
        return !blacklistPermissions.hasNode(permissionNode) && super.hasPermission(permissionNode);
    }
}
