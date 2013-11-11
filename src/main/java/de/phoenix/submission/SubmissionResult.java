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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.phoenix.rs.entity.PhoenixSubmissionResult;

public class SubmissionResult extends PhoenixSubmissionResult {

    @JsonIgnore
    private List<Object> temponaryResult = new ArrayList<Object>();

    public SubmissionResult() {
        super();
    }

    public SubmissionResult(SubmissionStatus status, String statusText) {
        super(status, statusText);
    }

    public SubmissionResult(SubmissionStatus newStatus, String newStatusText, SubmissionResult oldResult) {
        this(newStatus, newStatusText);
        add(oldResult.temponaryResult.toArray());
    }

    @JsonIgnore
    public List<Object> getTemponaryResult() {
        return temponaryResult;
    }

    public void add(Object... obj) {
        this.temponaryResult.addAll(Arrays.asList(obj));
    }
}
