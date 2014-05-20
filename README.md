# WebService

This is the server project for the Phoenix Project. The server manages all database entities and delivers the necessary information to the clients.

## Technology

The WebService is a JavaEE 6 application designed and running on a Glassfish 3. The communication between the server and its clients is REST based. To store all information a MySQL is necessary.
This project is developed as a Maven 3 project.

## Libraries

REST: Jersey 1.17 https://jersey.java.net/
JSON for REST: Faster-XML-Jackson https://github.com/FasterXML/jackson
JPA Library for MySQL Connection: Hibernate 3.6 http://hibernate.org/
Hashing and common codec function: Apache Commons Codec http://commons.apache.org/proper/commons-codec/

## Installation

1. Create a MySQL Database named 'phoenix'.
2. Execute the database dump "database/database.sql" to your created database for creating all necessary tables.
3. Create in your GlassFish a JDBC resource to this database.
4. Deploy the application to your GlassFish.
5. (Optional) Install website frontend - see https://github.com/Project-Phoenix/Website for further instructions.
