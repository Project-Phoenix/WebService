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

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.phoenix.rs.entity.PhoenixSubmissionResult;

/**
 * The result of a submission controlling process
 */
public class SubmissionResult extends PhoenixSubmissionResult {

    @JsonIgnore
    private Map<String, Object> temponaryResult = new HashMap<String, Object>();

    /**
     * Construct a submission result with a certain status and some detailled
     * information as text
     * 
     * @param status
     *            The status of the submission
     * @param statusText
     *            The detailled status text
     */
    public SubmissionResult(SubmissionStatus status, String statusText) {
        super(status, statusText);
    }

    /**
     * Construct a new submission result using the old submission result to copy
     * the temponary objects
     * 
     * @param newStatus
     *            The new status overriding the old one
     * @param newStatusText
     *            The new status text overriding the old one
     * @param oldResult
     *            The previous result
     */
    public SubmissionResult(SubmissionStatus newStatus, String newStatusText, SubmissionResult oldResult) {
        this(newStatus, newStatusText);
        this.temponaryResult.putAll(oldResult.temponaryResult);
    }

    @JsonIgnore
    public Map<String, Object> getTemponaryResult() {
        return temponaryResult;
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    public <T> T getTemponaryObject(String key) throws ClassCastException {
        return (T) temponaryResult.get(key);
    }

    public void add(String id, Object obj) {
        this.temponaryResult.put(id, obj);
    }
}
