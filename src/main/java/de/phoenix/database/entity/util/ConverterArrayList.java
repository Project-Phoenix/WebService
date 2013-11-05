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

package de.phoenix.database.entity.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Wrapper class to convert a list of elements implementing {@link Convertable}
 * to another type. <br>
 * This list uses an {@link ArrayList} as a base
 * 
 * @param <T>
 *            The targeted type
 */
public class ConverterArrayList<T> extends ArrayList<T> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an ArrayList from the collection and convert every element of
     * type E to type T
     * 
     * @param collection
     *            Collection of elements containing E, which implements
     *            {@link Convertable}
     */
    public <E extends Convertable<T>> ConverterArrayList(Collection<E> collection) {
        super(collection.size());
        for (E e : collection) {
            this.add(e.convert());
        }
    }
}
