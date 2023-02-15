import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            System.out.println("IP address : " + ip.getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Unable to get IP address: " + e.getMessage());
        }
    }
}
