import java.io.File;

public class DeleteFile {

    private int counter;

    public DeleteFile() {
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
