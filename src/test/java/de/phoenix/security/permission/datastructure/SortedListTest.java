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

import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class SortedListTest {

    @Test
    public void test() {
        SortedList<String> list = new SortedList<String>();

        // Generate test values - the alphabet in random order
        List<String> alphabet = new LinkedList<String>();
        for (int i = 0; i < 26; ++i) {
            alphabet.add("" + ((char) (i + 97)));
        }
        Collections.shuffle(alphabet);

        // Insert the chars
        for (String chars : alphabet) {
            list.add(chars);
        }
        checkList(list);

        // Add double values
        list.add("a");
        list.add("a");
        list.add("a");

        list.add("b");
        list.add("b");

        list.add("j");
        list.add("j");

        list.add("z");
        list.add("z");

        // check list
        checkList(list);

        list.clear();

        list.addAll(alphabet);

        checkList(list);
    }

    private void checkList(SortedList<String> list) {
        for (int i = 0; i < list.size() - 1; ++i) {
            if (list.get(i).compareTo(list.get(i + 1)) > 0)
                fail();
        }
    }
}
