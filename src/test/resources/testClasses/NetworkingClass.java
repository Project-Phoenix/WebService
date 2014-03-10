import java.net.*;

public class NetworkingClass {

    public NetworkingClass() {
        try {
            URL oracle = new URL("http://www.oracle.com/");
            URLConnection yc = oracle.openConnection();
        } catch(Exception){
            
        }
    }
}
