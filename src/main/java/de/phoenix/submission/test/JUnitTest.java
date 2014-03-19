package de.phoenix.submission.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to encapsulate a JUnit test with a certain timeout and placeholder to
 * replace at runtime
 */
public class JUnitTest {

    private String content;
    private final String className;

    /**
     * Construct the junit test with the classname of the junit test and the
     * content
     * 
     * @param className
     *            The name of the JUnit class
     * @param content
     *            The content of the JUnit class
     */
    private JUnitTest(String className, String content) {
        this.content = content;
        this.className = className;
    }

    /**
     * @return The name of the JUnit class
     */
    public String getClassName() {
        return className;
    }

    /**
     * @return The content of the Junit class
     */
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content.toString();
    }

    /**
     * Start the building process of a JUnit test
     * 
     * @param className
     *            The classname of the junit test
     * @param content
     *            The content of the junit test
     * @return A builder
     */
    public static JUnitTestBuilder create(String className, String content) {
        return new JUnitTestBuilder(className, content);
    }

    /**
     * Nested class for builder pattern
     */
    public static class JUnitTestBuilder {

        private JUnitTest product;

        private final static Pattern CLASS_TAG_PATTERN = Pattern.compile(Pattern.quote("${CLASS}"));

        private JUnitTestBuilder(String className, String content) {
            this.product = new JUnitTest(className, content);
        }

        /**
         * Replace all ${CLASS} with the to test class name
         * 
         * @param className
         *            The submitted class name
         * @return This builder
         */
        public JUnitTestBuilder setClassTag(String className) {
            this.product.content = CLASS_TAG_PATTERN.matcher(this.product.getContent()).replaceAll(className);
            return this;
        }

        /**
         * Set the time out for a test for all methods not further described by
         * a @Test(timeout) Annotation
         * 
         * @param timeout
         *            The timeout for the task in milliseconds
         * @return This builder
         */
        public JUnitTestBuilder setTimeOut(int timeout) {
            addImport("org.junit.Rule", "org.junit.rules.Timeout");

            int index = this.product.content.indexOf('{') + 1;
            StringBuilder sBuilder = new StringBuilder(this.product.content);
            sBuilder.insert(index, "\n\n@Rule\npublic Timeout globalTimeout = new Timeout(" + timeout + ");");
            this.product.content = sBuilder.toString();
            return this;
        }

        public JUnitTest build() {
            return this.product;
        }

        private void addImport(String... imports) {
            for (int i = 0; i < imports.length; ++i) {
                String string = imports[i];

                // The unit class has already the import
                if (this.product.content.contains(string))
                    continue;

                String lineSeparator = System.lineSeparator();
                string = "import " + string + ";" + lineSeparator;

                // add the import
                StringBuilder sBuilder = new StringBuilder(this.product.content);
                int start = importIndex();
                sBuilder.insert(start, string);
                this.product.content = sBuilder.toString();
            }
        }

        private final static Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([a-zA_Z_][\\.\\w]*);");

        private int importIndex() {

            Matcher matcher = PACKAGE_PATTERN.matcher(this.product.content);
            if (matcher.find())
                return matcher.end();
            else
                return 0;
        }

    }

}
