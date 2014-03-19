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

import java.util.List;

import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.database.entity.Text;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.submission.validate.CharSequenceValidator;
import de.phoenix.submission.validate.ContentValidateEngine;
import de.phoenix.submission.validate.ContentValidator.ContentValidatorResult;
import de.phoenix.submissionpipeline.UserSubmissionException;

/**
 * Submission controlling module to check, if the source code contaings
 * malicious or forbidden code fragements. <br>
 * This shall prevent code, that want to destroy something (delete files) or
 * open connections or something else, which can disturb the process
 */
public class SubmissionSourceValidator implements SubmissionHandler {

    @Override
    public PhoenixSubmissionResult controlSubmission(TaskSubmission submission) {
        DisallowedContent disallowedContent = submission.getTask().getDisallowedContent();
        if (disallowedContent == null) {
            return new PhoenixSubmissionResult(SubmissionStatus.SUBMITTED, "Submitted");
        }

        ContentValidateEngine engine = new ContentValidateEngine();
        for (String disallowedString : disallowedContent.getDisallowedContent()) {
            engine.registerContentChecker(new CharSequenceValidator(disallowedString));
        }

        // Check if the submission contains forbidden text
        List<Text> texts = submission.getTexts();
        for (Text text : texts) {
            ContentValidatorResult result = engine.validate(text.getContent());
            if (!result.isValid()) {
                throw new UserSubmissionException(result.getReason());
            }
        }

        return new PhoenixSubmissionResult(SubmissionStatus.SUBMITTED, "Submitted");
    }
}
