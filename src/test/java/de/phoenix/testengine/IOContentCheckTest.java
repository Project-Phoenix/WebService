package de.phoenix.testengine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.CharacterCodingException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.phoenix.submission.validate.CharSequenceValidator;
import de.phoenix.submission.validate.ContentValidator;
import de.phoenix.submission.validate.ContentValidator.ContentValidatorResult;
import de.phoenix.util.TextFileReader;

public class IOContentCheckTest {

    private static String maliciousSourceCode;
    private static String valideSourceCode;
    private static ContentValidator validator;

    @BeforeClass
    public static void beforeClass() throws CharacterCodingException {
        TextFileReader reader = new TextFileReader();
        maliciousSourceCode = reader.read(IOContentCheckTest.class.getResourceAsStream("/testClasses/DeleteFile.java"));
        valideSourceCode = reader.read(IOContentCheckTest.class.getResourceAsStream("/testClasses/MyBuilder.java"));
        validator = new CharSequenceValidator("java.io", "java.nio");
    }

    @Test
    public void simpleTest() {

        ContentValidatorResult result = validator.validateCode("import java.io");
        assertFalse(result.isValid());
        assertEquals("Code can not use java.io", result.getReason());

        result = validator.validateCode("import java.nio");
        assertFalse(result.isValid());
        assertEquals("Code can not use java.nio", result.getReason());

        result = validator.validateCode("import java.lang");
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }

    @Test
    public void maliciousFileTest() {

        ContentValidatorResult result = validator.validateCode(maliciousSourceCode);
        assertFalse(result.isValid());
        assertEquals("Code can not use java.io", result.getReason());
    }

    @Test
    public void valideFileTest() {

        ContentValidatorResult result = validator.validateCode(valideSourceCode);
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }
}
