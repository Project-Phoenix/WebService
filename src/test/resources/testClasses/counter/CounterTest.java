import static org.junit.Assert.*;
import org.junit.Test;

public class CounterTest {

    @Test
    public void test() {
        Counter counter = new Counter();
        assertEquals("Counter: 0", counter.toString());
        counter.count();
        counter.count();
        assertEquals("Counter: 2", counter.toString());
    }
}
