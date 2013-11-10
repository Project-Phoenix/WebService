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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.database.entity.Text;
import de.phoenix.rs.entity.PhoenixSubmission.SubmissionStatus;

public class SubmissionJavaCompiler implements SubmissionHandler {

    private JavaCompiler compiler;

    public SubmissionJavaCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        if (this.compiler == null) {
            throw new RuntimeException("No compiler was found - this handler will always return SubmissionStatus Errors!");
        }
    }
    @Override
    public SubmissionResult controlSubmission(TaskSubmission submission) {
        if (this.compiler == null) {
            return new SubmissionResult(SubmissionStatus.ERROR, "No compiler found");
        }

        // Convert texts to compileable obbjects
        List<SimpleJavaFileObject> compileObjects = prepareTexts(submission.getTexts());
        // Collector for compilation information
        DiagnosticCollector<Object> dia = new DiagnosticCollector<Object>();
        // Create task to compile
        CompilationTask task = compiler.getTask(null, null, dia, null, null, compileObjects);
        // Start compiling
        boolean result = task.call();

        // Print out results - if compilation was successfull, nothing happens
        List<Diagnostic<? extends Object>> diagnostics = dia.getDiagnostics();
        StringBuilder sBuilder = new StringBuilder();
        for (Diagnostic<? extends Object> diagnostic : diagnostics) {
            sBuilder.append(diagnostic);
            sBuilder.append("\n");
        }

        if (result)
            return new SubmissionResult(SubmissionStatus.COMPILED, "Kompiliert!");
        else
            return new SubmissionResult(SubmissionStatus.ERROR, sBuilder.toString());
    }

    /**
     * Convert the texts to compiable objects
     * 
     * @param texts
     *            The texts containing source code
     * @return The list containing compiable objects
     */
    private List<SimpleJavaFileObject> prepareTexts(List<Text> texts) {

        List<SimpleJavaFileObject> list = new ArrayList<SimpleJavaFileObject>(texts.size());
        for (Text text : texts) {
            list.add(new JavaObject(text.getName() + "." + text.getType(), text.getContent()));
        }

        return list;
    }

    /**
     * Wrapper class for compiable objects created just be strings without an
     * existing file source
     */
    private static class JavaObject extends SimpleJavaFileObject {

        private String content;

        public JavaObject(String className, String content) {
            super(URI.create(className), Kind.SOURCE);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }

    }

}
