package de.phoenix.testengine;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.nio.charset.CharacterCodingException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.phoenix.submission.validate.CharSequenceValidator;
import de.phoenix.submission.validate.ContentValidateEngine;
import de.phoenix.submission.validate.ContentValidator;
import de.phoenix.submission.validate.ContentValidator.ContentValidatorResult;
import de.phoenix.util.TextFileReader;

public class ValidatorEngineTest {

    private static TextFileReader reader;

    @BeforeClass
    public static void beforeClass() {
        reader = new TextFileReader();
    }

    @Test
    public void simpleIOTests() {

        ContentValidator validator = new CharSequenceValidator("java.io", "java.nio");

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
    public void fileBasedIOTests() throws FileNotFoundException, CharacterCodingException {

        ContentValidator validator = new CharSequenceValidator("java.io", "java.nio");
        ContentValidatorResult result = null;
        String valideCode = reader.read(getClass().getResourceAsStream("/testClasses/counter/goodImpl/Counter.java"));
        String maliciousCode = reader.read(getClass().getResourceAsStream("/testClasses/counter/malicousImpl/Counter.java"));

        result = validator.validateCode(maliciousCode);
        assertFalse(result.isValid());
        assertEquals("Code can not use java.io", result.getReason());

        result = validator.validateCode(valideCode);
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }

    @Test
    public void simpleNetworkTests() {
        ContentValidator validator = new CharSequenceValidator("java.net", "javax.net");
        ContentValidatorResult result = null;

        result = validator.validateCode("import java.net");
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
    public void fileBasedNetworkTests() throws FileNotFoundException, CharacterCodingException {
        ContentValidator validator = new CharSequenceValidator("java.net", "javax.net");
        ContentValidatorResult result = null;

        String maliciousCode = reader.read(getClass().getResourceAsStream("/testClasses/NetworkingClass.java"));
        String valideCode = reader.read(getClass().getResourceAsStream("/testClasses/builder/goodImpl/Builder.java"));

        result = validator.validateCode(maliciousCode);
        assertFalse(result.isValid());
        assertEquals("Code can not use java.net", result.getReason());

        result = validator.validateCode(valideCode);
        assertTrue(result.isValid());
        assertNull(result.getReason());
    }

    @Test
    public void simpleEngineTests() {
        ContentValidateEngine validatorEngine = new ContentValidateEngine();
        validatorEngine.registerContentChecker(new CharSequenceValidator("java.io", "java.nio"));
        validatorEngine.registerContentChecker(new CharSequenceValidator("java.net", "javax.net"));

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
    public void validateSourceCode() throws CharacterCodingException, FileNotFoundException {

        ContentValidateEngine validatorEngine = new ContentValidateEngine();
        validatorEngine.registerContentChecker(new CharSequenceValidator("java.io", "java.nio"));
        validatorEngine.registerContentChecker(new CharSequenceValidator("java.net", "javax.net"));

        String sourceCode = null;
        ContentValidatorResult result = null;

        sourceCode = reader.read(getClass().getResourceAsStream("/testClasses/counter/malicousImpl/Counter.java"));
        result = validatorEngine.validate(sourceCode);
        assertFalse(result.isValid());
        assertEquals("Code can not use java.io", result.getReason());

        sourceCode = reader.read(getClass().getResourceAsStream("/testClasses/counter/infiniteImpl/Counter.java"));
        result = validatorEngine.validate(sourceCode);
        assertTrue(result.isValid());

        sourceCode = reader.read(getClass().getResourceAsStream("/testClasses/builder/goodImpl/Builder.java"));
        result = validatorEngine.validate(sourceCode);
        assertTrue(result.isValid());

        sourceCode = reader.read(getClass().getResourceAsStream("/testClasses/counter/goodImpl/Counter.java"));
        result = validatorEngine.validate(sourceCode);
        assertTrue(result.isValid());

        sourceCode = reader.read(getClass().getResourceAsStream("/testClasses/NetworkingClass.java"));
        result = validatorEngine.validate(sourceCode);
        assertFalse(result.isValid());
        assertEquals("Code can not use java.net", result.getReason());
    }
}
