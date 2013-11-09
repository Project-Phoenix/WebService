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
import java.util.List;

import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.rs.entity.PhoenixSubmission.SubmissionStatus;

public class SubmissionController {

    private List<SubmissionControllable> submissionInspectors;

    public SubmissionController() {
        submissionInspectors = new ArrayList<SubmissionControllable>();
    }

    public void addChecker(SubmissionControllable submissionInspector) {
        this.submissionInspectors.add(submissionInspector);
    }

    public SubmissionControllResult controlSolution(TaskSubmission submission) {
        SubmissionControllResult status = null;
        for (SubmissionControllable check : submissionInspectors) {
            status = check.controlSubmission(submission);
            if (status.equals(SubmissionStatus.ERROR))
                break;
        }
        return status;
    }



}
