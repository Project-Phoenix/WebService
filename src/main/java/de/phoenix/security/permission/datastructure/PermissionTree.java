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

package de.phoenix.security.permission.datastructure;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PermissionTree implements Comparable<PermissionTree> {

    private PermissionTree root;
    private List<PermissionTree> childs;

    private final String node;

    public PermissionTree() {
        this(null, null);
    }

    private PermissionTree(final String node) {
        this(null, node);
    }

    private PermissionTree(final PermissionTree root, final String node) {
        this.root = root;
        this.node = node;
    }

    public PermissionTree getRoot() {
        return root;
    }

    public List<PermissionTree> getChilds() {
        return childs != null ? Collections.unmodifiableList(childs) : Collections.<PermissionTree> emptyList();
    }

    public String getNode() {
        return node;
    }

    private final static String WILDCARD = "*";
    private static final char NODE_SEPERATOR = '.';
    private final static PermissionTree WILDCARD_NODE = new PermissionTree(WILDCARD);

    private PermissionTree add(String node) {
        if (isLeaf())
            childs = new SortedList<PermissionTree>();

        if (node.equals(WILDCARD)) {
            childs.clear();
        }

        // There is already a wildcard - no need to add subnodes of a wildcard
        if (Collections.binarySearch(childs, WILDCARD_NODE) >= 0)
            return this;
        PermissionTree newNode = new PermissionTree(this, node);

        // Check if the node is already in the tree
        if (Collections.binarySearch(childs, newNode) >= 0)
            return this;
        else {
            // create new node
            childs.add(newNode);
            return newNode;
        }
    }

    public void addNode(String node) {
        // Split at first fullstop
        int pointIndex = node.indexOf(NODE_SEPERATOR);
        // Node is a leaf - insert at this tree
        if (pointIndex == -1)
            add(node);
        else {
            // Split node at first fullstop
            // Prefix
            String prefix = node.substring(0, pointIndex);
            String suffix = node.substring(pointIndex + 1);

            PermissionTree childNode = null;
            if (isLeaf()) {
                childNode = add(prefix);
            } else {
                // Prevent double nodes
                int i = Collections.binarySearch(childs, new PermissionTree(prefix));
                if (i < 0)
                    childNode = add(prefix);
                else
                    childNode = childs.get(i);
            }
            childNode.addNode(suffix);
        }
    }

    public boolean hasNode(String node) {

        if (isEmpty())
            return false;

        int pointIndex = node.indexOf(NODE_SEPERATOR);
        if (pointIndex == -1) {
            if (childs.size() == 1 && childs.get(0).getNode().equals(WILDCARD)) {
                return true;
            }
            // Node must be a child of this subtree
            return Collections.binarySearch(childs, new PermissionTree(node)) >= 0;
        } else {
            // Node must be a child of a subtree of this subtree

            // Split the node at the first .
            String prefix = node.substring(0, pointIndex);
            String suffix = node.substring(pointIndex + 1);
            if (childs.size() == 1 && childs.get(0).getNode().equals(WILDCARD)) {
                return true;
            }

            // Search for possible subtree with the node
            int i = Collections.binarySearch(childs, new PermissionTree(prefix));
            return i >= 0 ? childs.get(i).hasNode(suffix) : false;
        }
    }

    public boolean removeNode(String node) {
        if (isEmpty())
            return false;

        int pointIndex = node.indexOf(NODE_SEPERATOR);
        if (pointIndex == -1) {
            int i = Collections.binarySearch(childs, new PermissionTree(node));
            // Not in the tree
            if (i < 0)
                return false;
            else {
                childs.remove(i);
                if (childs.isEmpty())
                    childs = null;
                return true;
            }
        }
        String prefix = node.substring(0, pointIndex);
        String suffix = node.substring(pointIndex + 1);
        int i = Collections.binarySearch(childs, new PermissionTree(prefix));
        if (i < 0)
            return false;
        else
            return childs.get(i).removeNode(suffix);
    }

    public PermissionTree copy() {
        return copyAndAdd(new PermissionTree());
    }

    public PermissionTree copyAndAdd(final PermissionTree copy) {
        if (isEmpty())
            return copy;
        for (PermissionTree child : childs) {
            child.copy(copy, child.getNode());
        }
        return copy;
    }

    private void copy(final PermissionTree copy, String parentNode) {
        if (isLeaf()) {
            copy.addNode(parentNode);
        } else {
            for (PermissionTree child : childs) {
                child.copy(copy, parentNode + "." + child.getNode());
            }
        }
    }

    public boolean isEmpty() {
        return this.node == null && this.childs == null;
    }

    private boolean isLeaf() {
        return this.childs == null;
    }

    public List<String> toList() {
        if (isEmpty())
            return Collections.<String> emptyList();
        List<String> list = new LinkedList<String>();
        for (PermissionTree child : childs) {
            child.toList(list, child.getNode());
        }
        return list;
    }

    private void toList(final List<String> list, String parentNode) {
        if (isLeaf()) {
            list.add(parentNode);
        } else {
            for (PermissionTree child : childs) {
                child.toList(list, parentNode + "." + child.getNode());
            }
        }
    }

    @Override
    public String toString() {
        return node;
    }

    public int compareTo(PermissionTree other) {
        return this.node.compareTo(other.node);
    }
}
