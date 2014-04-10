import java.io.File;

public class Counter {

    private int counter;

    public Counter() {
        File file = new File("deleteme");
        file.delete();
    }

    public void count() {
        ++counter;
    }

    public String toString() {
        return "Counter: " + counter;
    }
}
