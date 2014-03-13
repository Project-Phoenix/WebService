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

package de.phoenix.submission;

/**
 * Subclass of {@link SubmissionException} to indicate errors, which are made by
 * the user (not reaching the test results or invalid java code)
 */
public class UserSubmissionException extends SubmissionException {

    private static final long serialVersionUID = -5030354193549941037L;

    public UserSubmissionException(String reason) {
        super(reason);
    }

}
