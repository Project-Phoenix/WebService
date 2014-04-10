public class Counter {

    private int counter;

    public Counter() {

    }

    public void count() {
        while (true)
            ++counter;
    }

    public String toString() {
        return "Counter: " + counter;
    }
}
