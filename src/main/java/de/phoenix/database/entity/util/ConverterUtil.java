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

package de.phoenix.database.entity.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class to convert Entities to PhoenixEntities
 */
public class ConverterUtil {

    private ConverterUtil() {

    }

    /**
     * Converts a list of entities to a list of phoenix entites.
     * 
     * @param list
     *            The list of entities
     * @return A list of phoenix entities
     */
    public static <T extends Convertable<E>, E> List<E> convert(List<T> list) {
        List<E> result = new ArrayList<E>();
        for (T tmp : list) {
            result.add(tmp.convert());
        }
        return result;
    }
}
