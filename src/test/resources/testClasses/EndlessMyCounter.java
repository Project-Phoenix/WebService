public class EndlessMyCounter {

    private int counter;

    public EndlessMyCounter() {

    }

    public void count() {
        while (true)
            ++counter;
    }

    public String toString() {
        return "Counter: " + counter;
    }
}
