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
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.submissionpipeline.SubmissionException;
import de.phoenix.submissionpipeline.UserSubmissionException;

public class SubmissionController {

    private List<SubmissionHandler> submissionHandler;

    /**
     * Create an empty submission controller doing nothing when
     * {@link #controllSolution(TaskSubmission)} is called
     */
    public SubmissionController() {
        submissionHandler = new ArrayList<SubmissionHandler>();
    }

    /**
     * Add an handler to the handler list. The order the handler are invoked is
     * the same they were added
     * 
     * @param handler
     *            New handler, will be added at last position of the list
     */
    public void addHandler(SubmissionHandler handler) {
        this.submissionHandler.add(handler);
    }

    /**
     * Start the controll mechansim of a submission. All added handlers are
     * invoked in the order they were added to this controller. <br>
     * When an error status was returned, the whole prozess is stopped and
     * returned an result containing the error message
     * 
     * @param submission
     *            The submission to becontrolled
     * @return SubmissionResult containg neccessary information
     */
    public PhoenixSubmissionResult controllSolution(TaskSubmission submission) {
        PhoenixSubmissionResult status = new PhoenixSubmissionResult(SubmissionStatus.SUBMITTED, "Submitted");

        for (SubmissionHandler handler : submissionHandler) {
            try {
                status = handler.controlSubmission(submission);
            } catch (UserSubmissionException e) {
                return new PhoenixSubmissionResult(SubmissionStatus.ERROR, e.getMessage());
            } catch (SubmissionException e) {
                return new PhoenixSubmissionResult(SubmissionStatus.ERROR, "Inform your admin - something went wrong you cannot fix yourself! Sry");
            }
        }
        return status;
    }

}
