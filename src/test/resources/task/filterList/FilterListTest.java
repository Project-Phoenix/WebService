import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class FilterListTest {

    @Test
    public void testStaticList() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        FilterList<Integer> filterList = new FilterList<Integer>(numbers);

        Predicate<Integer> pred = new EvenNumberPredicate();
        List<Integer> result = filterList.filter(pred);
        assertEquals(Arrays.asList(2, 4, 6, 8, 10), result);
    }

    @Test
    public void testPredicate() {
        Predicate<Integer> pred = new EvenNumberPredicate();
        assertTrue(pred.accecpt(2));
        assertFalse(pred.accecpt(3));
        assertFalse(pred.accecpt(200 * 2 + 1));
    }

    @Test
    public void testRandomList() {
        Random rand = new Random();
        int size = 1024;
        List<Integer> numbers = new ArrayList<Integer>(1024);
        for (int i = 0; i < size; ++i) {
            numbers.add(rand.nextInt(Integer.MAX_VALUE - 10));
        }

        FilterList<Integer> filterList = new FilterList<Integer>(numbers);

        List<Integer> result = filterList.filter(new EvenNumberPredicate());
        for (Integer integer : result) {
            if (integer % 2 == 1)
                fail("odd number : " + integer);
        }
    }

}
