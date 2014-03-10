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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class PermissionTreeTest {

    private List<String> readAllLines(File f) throws Exception {
        List<String> lines = new ArrayList<String>();
        BufferedReader bReader = new BufferedReader(new FileReader(f));
        String line = "";
        while ((line = bReader.readLine()) != null)
            lines.add(line);
        bReader.close();
        return lines;
    }

    @Test
    public void createBigTree() {
        try {
            File f = new File("src/test/resources/security/permissions.txt");
            List<String> lines = readAllLines(f);
            PermissionTree tree = new PermissionTree();
            for (String line : lines) {
                tree.addNode(line);
            }

            for (String line : lines) {
                assertTrue(tree.hasNode(line));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void cloneTest() {
        try {
            File f = new File("src/test/resources/security/permissions.txt");
            List<String> lines = readAllLines(f);
            PermissionTree tree = new PermissionTree();
            for (String line : lines) {
                tree.addNode(line);
            }

            PermissionTree copy = tree.copy();
            assertTrue(toStringTree(tree).equals(toStringTree(copy)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simplePermissionTest() {
        PermissionTree tree = new PermissionTree();
        tree.addNode("lol.test");
        tree.addNode("lol.test2");
        assertTrue(tree.hasNode("lol.test"));
        assertFalse(tree.hasNode("lol.test3"));
    }

    @Test
    public void wildcardTest() {
        PermissionTree tree = new PermissionTree();
        tree.addNode("lol.test");
        tree.addNode("lol.test2");
        tree.addNode("lol.*");
        tree.addNode("lol.test");

        assertTrue(tree.hasNode("lol.test.herp"));
        assertTrue(tree.hasNode("lol.test2"));
        assertFalse(tree.hasNode("*"));

        tree.addNode("*");
        assertTrue(tree.hasNode("lol.test.herp"));
        assertTrue(tree.hasNode("lol.test2"));
        assertTrue(tree.hasNode("*"));
    }

    @Test
    public void removeTest() {
        PermissionTree tree = new PermissionTree();
        tree.addNode("test.lol1");
        tree.addNode("test.lol2");
        tree.addNode("test.lol3");

        assertTrue(tree.hasNode("test.lol1"));
        assertTrue(tree.hasNode("test.lol2"));
        assertTrue(tree.hasNode("test.lol3"));

        assertTrue(tree.removeNode("test.lol1"));
        assertFalse(tree.hasNode("test.lol1"));

        assertTrue(tree.removeNode("test"));
        assertFalse(tree.hasNode("test"));
    }

    @Test
    public void toListTest() {
        try {
            File f = new File("src/test/resources/security/permissions.txt");
            List<String> lines = readAllLines(f);
            PermissionTree tree = new PermissionTree();
            for (String line : lines) {
                tree.addNode(line);
            }

            List<String> nodes = tree.toList();
            for (String node : nodes)
                assertTrue(tree.hasNode(node));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Start of
    // (C) Connor Garvey at January 30th, 2009.
    public static String toStringTree(PermissionTree node) {
        final StringBuilder buffer = new StringBuilder();
        return toStringTreeHelper(node, buffer, new LinkedList<Iterator<PermissionTree>>()).toString();
    }

    private static String toStringTreeDrawLines(List<Iterator<PermissionTree>> parentIterators, boolean amLast) {
        StringBuilder result = new StringBuilder();
        Iterator<Iterator<PermissionTree>> it = parentIterators.iterator();
        while (it.hasNext()) {
            Iterator<PermissionTree> anIt = it.next();
            if (anIt.hasNext() || (!it.hasNext() && amLast)) {
                result.append("   |");
            } else {
                result.append("    ");
            }
        }
        return result.toString();
    }

    private static StringBuilder toStringTreeHelper(PermissionTree node, StringBuilder buffer, List<Iterator<PermissionTree>> parentIterators) {
        if (!parentIterators.isEmpty()) {
            boolean amLast = !parentIterators.get(parentIterators.size() - 1).hasNext();
            buffer.append("\n");
            String lines = toStringTreeDrawLines(parentIterators, amLast);
            buffer.append(lines);
            buffer.append("\n");
            buffer.append(lines);
            buffer.append("- ");
        }
        buffer.append(node.toString());
        if (node.getChilds() != null) {
            Iterator<PermissionTree> it = node.getChilds().iterator();
            parentIterators.add(it);

            while (it.hasNext()) {
                PermissionTree child = it.next();
                toStringTreeHelper(child, buffer, parentIterators);
            }
            parentIterators.remove(it);
        }
        return buffer;
    }
    // End of
    // (C) Connor Garvey at January 30th, 2009.

}
