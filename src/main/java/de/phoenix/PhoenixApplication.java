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

package de.phoenix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.ApplicationPath;

import com.sun.jersey.api.core.PackagesResourceConfig;

import de.phoenix.database.entity.DebugLog;

@ApplicationPath("/rest")
public class PhoenixApplication extends PackagesResourceConfig {

    public static File submissionPipelineDir;
    public static File submissionPipelineFile;

    public PhoenixApplication() {
        super("de.phoenix.webresource", "de.phoenix.rs");

        try {
            extractSubmissionPipeline();
        } catch (IOException e) {
            DebugLog.log(e);
        }

    }

    private void extractSubmissionPipeline() throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("../lib/SubmissionPipeline-0.0.1-SNAPSHOT.jar");
        if (in == null) {
            DebugLog.log("Can't find the submission pipeline program!");
            return;
        }

        submissionPipelineDir = new File(System.getProperty("java.io.tmpdir"), "phoenixhelper");
        submissionPipelineDir.mkdir();

        submissionPipelineFile = new File(submissionPipelineDir, "SubmissionPipeline-0.0.1-SNAPSHOT.jar");

        OutputStream out = new FileOutputStream(submissionPipelineFile);
        byte[] buffer = new byte[2048];
        int read = 0;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.close();
        in.close();

    }

}
