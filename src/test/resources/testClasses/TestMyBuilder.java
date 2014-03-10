import static org.junit.Assert.*;

import org.junit.Test;
public class TestMyBuilder {

    @Test
    public void test() {
        MyBuilder m = new MyBuilder();
        m.append("Hello World");
        assertEquals("Hello World",m.toString());
    }
}
