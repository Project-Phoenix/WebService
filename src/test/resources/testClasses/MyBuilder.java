public class MyBuilder {

    private String buffer;

    public MyBuilder() {
        this.buffer = "";
    }

    public MyBuilder append(String string) {
        buffer += string;
        return this;
    }

    public String toString() {
        return buffer;
    }
}
