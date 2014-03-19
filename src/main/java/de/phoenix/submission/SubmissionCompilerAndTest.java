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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.phoenix.PhoenixApplication;
import de.phoenix.database.entity.Attachment;
import de.phoenix.database.entity.DebugLog;
import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.database.entity.TaskTest;
import de.phoenix.database.entity.Text;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.submission.test.JUnitTest;
import de.phoenix.submissionpipeline.UserSubmissionException;
import de.phoenix.submissionpipeline.api.SubmissionTask;

public class SubmissionCompilerAndTest implements SubmissionHandler {

    private final static ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Override
    public PhoenixSubmissionResult controlSubmission(TaskSubmission submission) {

        SubmissionTask task = new SubmissionTask();

        File dir = PhoenixApplication.submissionPipelineDir;
        List<String> commands = getCommands();

        // TODO: Support multiple texts to compile in the future!
        if (submission.getTexts().size() > 1) {
            throw new UserSubmissionException("Multiple submission texts are currently not supported!");
        }
        // TODO: Assign class name to replace tag!
        String className = submission.getTexts().get(0).getTitle();

        for (Text clazz : submission.getTexts()) {
            addClass(task, clazz);
        }

        DebugLog.log(submission.getTask().isAutomaticTest()+"");
        if (submission.getTask().isAutomaticTest()) {
            for (TaskTest test : submission.getTask().getTaskTests()) {
                addTest(task, test, className);
            }
        }
        
        DebugLog.log(task.getTests().toString());

        // TODO: Add libraries

        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.directory(dir);

        File errorLog = new File(dir, "error.log");
        errorLog.delete();
        builder.redirectError(errorLog);

        try {
            Process process = builder.start();
            DebugLog.log("Started process");
            JSON_MAPPER.writeValue(process.getOutputStream(), task);
            DebugLog.log("Wrote task to process");
            process.getOutputStream().close();
            DebugLog.log("Closed output stream");

            DebugLog.log("Waiting for result");
            PhoenixSubmissionResult result = JSON_MAPPER.readValue(process.getInputStream(), PhoenixSubmissionResult.class);
            DebugLog.log("Received result: " + result);

            return result;
        } catch (Exception e) {
            DebugLog.log(e);
        }

        return new PhoenixSubmissionResult(SubmissionStatus.OK, "Fine");
    }

    private List<String> getCommands() {
        List<String> commands = new ArrayList<String>();

        commands.add("java");
        commands.add("-jar");
        commands.add(PhoenixApplication.submissionPipelineFile.getAbsolutePath());

        return commands;
    }

    private void addClass(SubmissionTask task, Text clazz) {
        task.addClass(clazz.convert());
    }

    private void addTest(SubmissionTask task, TaskTest test, String className) {
        Text text = test.getText();
        JUnitTest unitTest = JUnitTest.create(text.getTitle(), text.getContent()).setClassTag(className).setTimeOut(test.getTimeout()).build();
        task.addTest(new PhoenixText(unitTest.getContent(), DateTime.now(), unitTest.getClassName(), "java"));
    }

    @SuppressWarnings("unused")
    private void addLibrary(SubmissionTask task, Attachment library) {
        task.addLibrary(library.convert());
    }
}
