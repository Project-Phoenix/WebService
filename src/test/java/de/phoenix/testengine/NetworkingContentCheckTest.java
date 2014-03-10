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

public class NetworkingContentCheckTest {

    private static String maliciousSourceCode;
    private static String valideSourceCode;
    private static ContentValidator validator;

    @BeforeClass
    public static void beforeClass() throws CharacterCodingException {
        TextFileReader reader = new TextFileReader();
        maliciousSourceCode = reader.read(NetworkingContentCheckTest.class.getResourceAsStream("/testClasses/NetworkingClass.java"));
        valideSourceCode = reader.read(NetworkingContentCheckTest.class.getResourceAsStream("/testClasses/MyBuilder.java"));
        validator = new CharSequenceValidator("java.net", "javax.net");
    }

    @Test
    public void simpleTest() {

        ContentValidatorResult result = validator.validateCode("import java.net");
        assertFalse(result.isValid());
        assertEquals("Code can not use java.net", result.getReason());

        result = validator.validateCode("import javax.net");
        assertFalse(result.isValid());
        assertEquals("Code can not use javax.net", result.getReason());

        result = validator.validateCode("import java.lang");
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }

    @Test
    public void maliciousFileTest() {

        ContentValidatorResult result = validator.validateCode(maliciousSourceCode);
        assertFalse(result.isValid());
        assertEquals("Code can not use java.net", result.getReason());
    }

    @Test
    public void valideFileTest() {

        ContentValidatorResult result = validator.validateCode(valideSourceCode);
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }
}
