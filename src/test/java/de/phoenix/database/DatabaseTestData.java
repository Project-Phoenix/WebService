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

package de.phoenix.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.jdbc.Work;

import de.phoenix.database.DatabaseManager;

public class DatabaseTestData {

    private final static DatabaseTestData instance = new DatabaseTestData();

    private DatabaseTestData() {
    }

    public static DatabaseTestData getInstance() {
        return instance;
    }

    public void createTestData() {

        DatabaseManager.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {

                System.out.println("Prepare database");
                Statement stmt = connection.createStatement();
                stmt.execute("SET UNIQUE_CHECKS=0");
                stmt.execute("SET FOREIGN_KEY_CHECKS=0");
                ResultSet resultSet = stmt.executeQuery("SELECT @@sql_mode;");
                resultSet.next();
                String oldMode = resultSet.getString(1);
                stmt.execute("SET SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES'");
                stmt.close();

                System.out.println("Start executing dump");
                ScriptRunner r = new ScriptRunner(connection);
                try {
                    r.runScript(new InputStreamReader(new FileInputStream(new File("src/test/resources/database/testdata.sql")), Charset.forName("UTF-8")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Finished executing dump!");

                stmt = connection.createStatement();
                stmt.execute("SET UNIQUE_CHECKS=1");
                stmt.execute("SET FOREIGN_KEY_CHECKS=1");
                stmt.execute("SET SQL_MODE='" + oldMode + "'");
                stmt.close();
            };
        });

        System.out.println("Finished!");

    }

    private class ScriptRunner {

        private static final String DEFAULT_DELIMITER = ";";

        private Connection connection;

        private PrintWriter logWriter = new PrintWriter(System.out);
        private PrintWriter errorLogWriter = new PrintWriter(System.err);

        private String delimiter = DEFAULT_DELIMITER;
        private boolean fullLineDelimiter = false;

        /**
         * Default constructor
         */
        public ScriptRunner(Connection connection) {
            this.connection = connection;
        }

        /**
         * Runs an SQL script (read in using the Reader parameter)
         * 
         * @param reader
         *            - the source of the script
         */
        public void runScript(Reader reader) throws IOException, SQLException {
            try {
                boolean originalAutoCommit = connection.getAutoCommit();
                try {

                    runScript(connection, reader);
                } finally {
                    connection.setAutoCommit(originalAutoCommit);
                }
            } catch (IOException e) {
                throw e;
            } catch (SQLException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Error running script.  Cause: " + e, e);
            }
        }

        /**
         * Runs an SQL script (read in using the Reader parameter) using the
         * connection passed in
         * 
         * @param conn
         *            - the connection to use for the script
         * @param reader
         *            - the source of the script
         * @throws SQLException
         *             if any SQL errors occur
         * @throws IOException
         *             if there is an error reading from the Reader
         */
        private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
            StringBuffer command = null;
            try {
                LineNumberReader lineReader = new LineNumberReader(reader);
                String line = null;
                while ((line = lineReader.readLine()) != null) {
                    if (command == null) {
                        command = new StringBuffer();
                    }
                    String trimmedLine = line.trim();
                    if (trimmedLine.startsWith("--")) {
                        println(trimmedLine);
                    } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
                        // Do nothing
                    } else if (trimmedLine.length() < 1 || trimmedLine.startsWith("--")) {
                        // Do nothing
                    } else if (!fullLineDelimiter && trimmedLine.endsWith(getDelimiter()) || fullLineDelimiter && trimmedLine.equals(getDelimiter())) {
                        command.append(line.substring(0, line.lastIndexOf(getDelimiter())));
                        command.append(" ");
                        Statement statement = conn.createStatement();

                        try {
                            statement.execute(command.toString());
                        } catch (SQLException e) {
                            e.fillInStackTrace();
                            printlnError("Error executing: " + command);
                            printlnError(e);
                        }

                        conn.commit();

                        command = null;
                        try {
                            statement.close();
                        } catch (Exception e) {
                            // Ignore to workaround a bug in Jakarta DBCP
                        }
                        Thread.yield();
                    } else {
                        command.append(line);
                        command.append(" ");
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                e.fillInStackTrace();
                printlnError("Error executing: " + command);
                printlnError(e);
                throw e;
            } catch (IOException e) {
                e.fillInStackTrace();
                printlnError("Error executing: " + command);
                printlnError(e);
                throw e;
            } finally {
                flush();
            }
        }

        private String getDelimiter() {
            return delimiter;
        }

        private void println(Object o) {
            if (logWriter != null) {
                logWriter.println(o);
            }
        }

        private void printlnError(Object o) {
            if (errorLogWriter != null) {
                errorLogWriter.println(o);
            }
        }

        private void flush() {
            if (logWriter != null) {
                logWriter.flush();
            }
            if (errorLogWriter != null) {
                errorLogWriter.flush();
            }
        }
    }

}
