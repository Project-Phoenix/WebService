package de.phoenix.submission.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import de.phoenix.submission.compiler.CharSequenceCompiler;
import de.phoenix.submission.compiler.CharSequenceCompilerException;

public class JUnitTest {

    private String content;
    private final String className;

    private JUnitTest(String className, String content) {
        this.content = content;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content.toString();
    }

    public <T> Class<T> compile(CharSequenceCompiler<T> compiler) throws ClassCastException, CharSequenceCompilerException {
        return compiler.compile(className, content);
    }

    public <T> Result runTest(CharSequenceCompiler<T> compiler) throws ClassCastException, CharSequenceCompilerException {
        Class<T> compiledClass = compile(compiler);
        return JUnitCore.runClasses(compiledClass);
    }

    public static JUnitTestBuilder create(String className, String content) {
        return new JUnitTestBuilder(className, content);
    }

    public static class JUnitTestBuilder {

        private JUnitTest product;

        private final static Pattern CLASS_TAG_PATTERN = Pattern.compile(Pattern.quote("${CLASS}"));

        private JUnitTestBuilder(String className, String content) {
            this.product = new JUnitTest(className, content);
        }

        public JUnitTestBuilder setClassTag(String className) {
            this.product.content = CLASS_TAG_PATTERN.matcher(this.product.getContent()).replaceAll(className);
            return this;
        }

        public JUnitTestBuilder setTimeOut(int timeout) {
            organizeImports("org.junit.Rule", "org.junit.rules.Timeout");

            int index = this.product.content.indexOf('{') + 1;
            StringBuilder sBuilder = new StringBuilder(this.product.content);
            sBuilder.insert(index, "\n\n@Rule\npublic Timeout globalTimeout = new Timeout(" + timeout + ");");
            this.product.content = sBuilder.toString();
            return this;
        }
        public JUnitTest build() {
            return this.product;
        }

        private void organizeImports(String... imports) {
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
