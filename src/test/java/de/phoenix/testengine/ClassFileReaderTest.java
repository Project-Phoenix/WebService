package de.phoenix.testengine;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.CharacterCodingException;

import org.junit.Test;

import de.phoenix.util.TextFileReader;

public class ClassFileReaderTest {

    @Test
    public void testFileFromStream() throws CharacterCodingException {
        TextFileReader cReader = new TextFileReader();
        assertNotNull(cReader);

        String s = cReader.read(getClass().getResourceAsStream("/testClasses/MyCounter.java"));
        assertNotNull(s);
        assertFalse(s.isEmpty());
        assertTrue(s.contains("public class MyCounter"));
    }

}
