import static org.junit.Assert.*;

import org.junit.Test;
public class BuilderTest {

    @Test
    public void test() {
        Builder m = new Builder();
        m.append("Hello World");
        assertEquals("Hello World",m.toString());
    }
}
