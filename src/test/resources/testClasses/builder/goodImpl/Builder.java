public class Builder {

    private String buffer;

    public Builder() {
        this.buffer = "";
    }

    public Builder append(String string) {
        buffer += string;
        return this;
    }

    public String toString() {
        return buffer;
    }
}