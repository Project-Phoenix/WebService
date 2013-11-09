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

public class SubmissionCompiler implements SubmissionControllable {

    private JavaCompiler compiler;

    public SubmissionCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
    }

    @Override
    public SubmissionControllResult controlSubmission(TaskSubmission submission) {
        List<SimpleJavaFileObject> compileObjects = prepareTexts(submission.getTexts());
        DiagnosticCollector<Object> dia = new DiagnosticCollector<Object>();
        CompilationTask task = compiler.getTask(null, null, dia, null, null, compileObjects);
        boolean result = task.call();

        List<Diagnostic<? extends Object>> diagnostics = dia.getDiagnostics();
        StringBuilder sBuilder = new StringBuilder();
        for (Diagnostic<? extends Object> diagnostic : diagnostics) {
            sBuilder.append(diagnostic);
            sBuilder.append("\n");
        }
        if (result)
            return new SubmissionControllResult(SubmissionStatus.COMPILED, "Kompiliert!");
        else
            return new SubmissionControllResult(SubmissionStatus.ERROR, sBuilder.toString());
    }

    private List<SimpleJavaFileObject> prepareTexts(List<Text> texts) {

        List<SimpleJavaFileObject> list = new ArrayList<SimpleJavaFileObject>(texts.size());
        for (Text text : texts) {
            list.add(new JavaObject(text.getName() + "." + text.getType(), text.getContent()));
        }

        return list;
    }

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
