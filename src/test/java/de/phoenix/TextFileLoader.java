package de.phoenix;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class TextFileLoader {

    private Charset charset;

    public TextFileLoader() {
        this("UTF-8");
    }

    public TextFileLoader(String charset) {
        this.charset = Charset.forName(charset);
    }

    public String readFile(InputStream inputStream) {
        return readFileContent(new InputStreamReader(inputStream, charset));
    }

    public String readFile(String fileName) throws FileNotFoundException {
        return readFile(new File(fileName));
    }

    public String readFile(File file) throws FileNotFoundException {
        return readFile(new FileInputStream(file));
    }

    private String readFileContent(InputStreamReader reader) {
        try {
            BufferedReader bReader = new BufferedReader(reader);
            String lineSeparator = System.getProperty("line.separator");
            StringBuilder sBuilder = new StringBuilder();

            String line = "";
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line);
                sBuilder.append(lineSeparator);
            }

            sBuilder.delete(sBuilder.length() - 1, sBuilder.length());

            bReader.close();
            return sBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
