package de.phoenix.testengine;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.phoenix.TextFileLoader;

public class ClassFileReaderTest {

    @Test
    public void testFileFromStream() {
        TextFileLoader cReader = new TextFileLoader();
        assertNotNull(cReader);

        String s = cReader.readFile(getClass().getResourceAsStream("/testClasses/MyCounter.java"));
        assertNotNull(s);
        assertFalse(s.isEmpty());
        assertTrue(s.contains("public class MyCounter"));
    }

}
