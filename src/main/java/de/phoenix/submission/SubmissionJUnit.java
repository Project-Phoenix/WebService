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

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import de.phoenix.database.entity.Task;
import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.database.entity.Text;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.submission.compiler.CharSequenceCompiler;
import de.phoenix.submission.compiler.CharSequenceCompilerException;
import de.phoenix.submission.test.JUnitTest;

public class SubmissionJUnit implements SubmissionHandler {

    private static final int DEFAULT_TIMEOUT = 10000;

    @Override
    public SubmissionResult controlSubmission(TaskSubmission submission, SubmissionResult predecessorResult) {

        CharSequenceCompiler<Object> compiler = predecessorResult.getTemponaryObject("compiler");
        if (compiler == null) {
            throw new RuntimeException("Can't find compiler");
        }
        Task task = submission.getTask();

        List<Text> tests = task.getTests();
        if (tests.isEmpty()) {
            return new SubmissionResult(SubmissionStatus.OK, "No tests - everything ok");
        }
        if (tests.size() > 1) {
            throw new RuntimeException("Currently only one test is supported!");
        }
        if (submission.getTexts().size() > 1) {
            throw new RuntimeException("Currently only one submitted class is supported!");
        }

        Text test = tests.get(0);
        try {
            JUnitTest unitTest = prepareJunit(test, submission.getTexts().get(0), DEFAULT_TIMEOUT);

            Class<Object> testClass = unitTest.compile(compiler);
            Result testResult = JUnitCore.runClasses(testClass);
            if (testResult.getFailureCount() == 0)
                return new SubmissionResult(SubmissionStatus.OK, "Submission passed all tests!", predecessorResult);
            else
                return new SubmissionResult(SubmissionStatus.TEST_FAILED, testResult.getFailures().toString(), predecessorResult);

        } catch (CharSequenceCompilerException e) {
            List<Diagnostic<? extends JavaFileObject>> diagnostics = e.getDiagnostics().getDiagnostics();
            return new SubmissionResult(SubmissionStatus.ERROR, diagnostics.toString());
        }
    }

    private JUnitTest prepareJunit(Text test, Text submittedClass, int timeout) {
        return JUnitTest.create(test.getName(), test.getContent()).setClassTag(submittedClass.getName()).setTimeOut(timeout).build();
    }

}
