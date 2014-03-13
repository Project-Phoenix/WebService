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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import de.phoenix.database.entity.TaskSubmission;
import de.phoenix.database.entity.Text;
import de.phoenix.rs.entity.PhoenixSubmissionResult.SubmissionStatus;
import de.phoenix.submission.compiler.CharSequenceCompiler;
import de.phoenix.submission.compiler.CharSequenceCompilerException;

/**
 * Submission controlling module to compile java source files. <br>
 * This class uses a in memory compiler, the {@link CharSequenceCompiler}
 */
public class SubmissionJavaCompiler implements SubmissionHandler {

    public SubmissionJavaCompiler() {
        if (ToolProvider.getSystemJavaCompiler() == null) {
            throw new RuntimeException("No compiler was found - this handler will always return SubmissionStatus Errors!");
        }
    }

    @Override
    public SubmissionResult controlSubmission(TaskSubmission submission, SubmissionResult predecessorStatus) {
        CharSequenceCompiler<Object> compiler;
        try {
            compiler = new CharSequenceCompiler<Object>();
        } catch (IllegalStateException e) {
            // Thrown when the compiler is not found
            throw new SubmissionException("Can't find compiler!");
        }

        Map<String, CharSequence> classesToCompile = prepareTexts(submission.getTexts());
        Map<String, Class<Object>> compiledClasses = null;

        try {
            compiledClasses = compiler.compile(classesToCompile);
        } catch (CharSequenceCompilerException e) {
            // Compile error - collect all errors and send back to user
            List<Diagnostic<? extends JavaFileObject>> diagnostics = e.getDiagnostics().getDiagnostics();
            throw new UserSubmissionException(diagnostics.toString());
        }

        SubmissionResult res = new SubmissionResult(SubmissionStatus.COMPILED, "Kompiliert!", predecessorStatus);
        res.add("compiledClasses", compiledClasses);
        res.add("compiler", compiler);
        return res;
    }

    /**
     * Convert the texts to compiable objects
     * 
     * @param texts
     *            The texts containing source code
     * @return The list containing compiable objects
     */
    private Map<String, CharSequence> prepareTexts(List<Text> texts) {
        Map<String, CharSequence> classesToCompile = new LinkedHashMap<String, CharSequence>(texts.size(), 1.0f);
        for (Text text : texts) {
            classesToCompile.put(text.getTitle(), text.getContent());
        }

        return classesToCompile;
    }
}
