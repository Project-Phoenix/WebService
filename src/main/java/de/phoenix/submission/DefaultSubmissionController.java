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

package de.phoenix.submission;

public class DefaultSubmissionController extends SubmissionController {

    /**
     * Default submission controller. First check if the source contains
     * forbidden code fragements, then compile and after this unit test it
     */
    public DefaultSubmissionController() {
        super();
        addHandler(new SubmissionSourceValidator());
        addHandler(new SubmissionCompilerAndTest());
    }

}
