import static org.junit.Assert.*;
import org.junit.Test;

public class TestCounter {

    @Test
    public void test() {
        ${CLASS} counter = new ${CLASS}();
        assertEquals("Counter: 0", counter.toString());
        counter.count();
        counter.count();
        assertEquals("Counter: 2", counter.toString());
    }
}
