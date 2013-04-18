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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A simple implementation of an sorted list based on an array
 * 
 * @param <T>
 */
public class SortedList<T extends Comparable<T>> extends ArrayList<T> {

    private static final long serialVersionUID = -441423857950125427L;

    /**
     * Add the element to this sorted list in a sorted order.
     * 
     * @param e
     *            The element
     * @return Always true
     * 
     */
    @Override
    public boolean add(T e) {

        int insertPoint = Collections.binarySearch(this, e);
        if (insertPoint < 0)
            insertPoint = (insertPoint * -1) - 1;
        super.add(insertPoint, e);
        return true;

    }

    /**
     * 
     * @deprecated Function ignores the index and insert the element in the
     *             correct order
     */
    @Deprecated
    public void add(int index, T element) {
        add(element);
    }

    /**
     * Add all elements from the collection to this sorted list in a sorted
     * order.
     * 
     * @param c
     *            The collection holding all elements to add to this sorted
     *            list. The elements in the collection needn't to be in sorted
     *            order
     * @return Always true
     * 
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T s : c)
            add(s);
        return true;
    }

    /**
     * 
     * @deprecated Function ignores the index and insert the elements in the
     *             correct order
     */
    @Deprecated
    public boolean addAll(int index, Collection<? extends T> c) {
        return addAll(c);
    }
}
