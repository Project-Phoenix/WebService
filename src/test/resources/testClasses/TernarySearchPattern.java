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

import org.junit.Assert.*;

public class TernarySearchPattern {

    public void test() {
        int[] testArray = {1,2,3,4,5,6,7,8,9,10};
        int pos = ${CLASS}.ternaryRec(testArray, 5);
        assertEquals(4, pos);
    }
}
