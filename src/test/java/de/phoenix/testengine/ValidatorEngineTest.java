package de.phoenix.testengine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.CharacterCodingException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.phoenix.submission.validate.CharSequenceValidator;
import de.phoenix.submission.validate.ContentValidateEngine;
import de.phoenix.submission.validate.ContentValidator.ContentValidatorResult;
import de.phoenix.util.TextFileReader;

public class ValidatorEngineTest {

    private static ContentValidateEngine validatorEngine;
    private static TextFileReader reader;

    @BeforeClass
    public static void beforeClass() {
        reader = new TextFileReader();

        validatorEngine = new ContentValidateEngine();
        validatorEngine.registerContentChecker(new CharSequenceValidator("java.io", "java.nio"));
        validatorEngine.registerContentChecker(new CharSequenceValidator("java.net", "javax.net"));
    }

    @Test
    public void simpleTests() {
        ContentValidatorResult result = validatorEngine.validate("import java.io");
        assertFalse(result.isValid());
        assertEquals("Code can not use java.io", result.getReason());

        result = validatorEngine.validate("import javax.net");
        assertFalse(result.isValid());
        assertEquals("Code can not use javax.net", result.getReason());

        result = validatorEngine.validate("import java.math");
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }

    @Test
    public void validateSourceCode() throws CharacterCodingException {
        String sourceCode = null;
        ContentValidatorResult result = null;

        sourceCode = reader.read(getClass().getResourceAsStream("/testClasses/DeleteFile.java"));
        result = validatorEngine.validate(sourceCode);
        assertFalse(result.isValid());
        assertEquals("Code can not use java.io", result.getReason());

        sourceCode = reader.read(getClass().getResourceAsStream("/testClasses/EndlessMyCounter.java"));
        result = validatorEngine.validate(sourceCode);
        assertTrue(result.isValid());

        sourceCode = reader.read(getClass().getResourceAsStream("/testClasses/MyBuilder.java"));
        result = validatorEngine.validate(sourceCode);
        assertTrue(result.isValid());

        sourceCode = reader.read(getClass().getResourceAsStream("/testClasses/MyCounter.java"));
        result = validatorEngine.validate(sourceCode);
        assertTrue(result.isValid());

        sourceCode = reader.read(getClass().getResourceAsStream("/testClasses/NetworkingClass.java"));
        result = validatorEngine.validate(sourceCode);
        assertFalse(result.isValid());
        assertEquals("Code can not use java.net", result.getReason());
    }

}
