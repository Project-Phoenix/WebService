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
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.submission.validate.CharSequenceValidator;
import de.phoenix.submission.validate.ContentValidateEngine;
import de.phoenix.submission.validate.ContentValidator.ContentValidatorResult;

public class SubmissionSourceValidator implements SubmissionHandler {

    @Override
    public SubmissionResult controlSubmission(TaskSubmission submission, SubmissionResult predecessorResult) {

        ContentValidateEngine engine = new ContentValidateEngine();
        // TODO: Do this dynamically from the test !
        engine.registerContentChecker(new CharSequenceValidator("java.net", "javax.net", "java.io", "java.nio"));

        // Check if the submission contains forbidden text
        List<Text> texts = submission.getTexts();
        for (Text text : texts) {
            ContentValidatorResult result = engine.validate(text.getContent());
            if (!result.isValid()) {
                throw new UserSubmissionException(result.getReason());
            }
        }

        return new SubmissionResult(SubmissionStatus.SUBMITTED, "Submitted", predecessorResult);
    }
}
