import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        IpAddress myIp = new IpAddress();
        String result = myIp.scan();
        if (result.isEmpty()) {
            System.out.println("Systen ip not detected");
        } else {
            System.out.println("My ip address : " + result);
            Network mynetwork = new Network(result);
            mynetwork.scan(1);
        }
    }
}

class IpAddress{
    public String scan(){
        String address = "null";
        try {
            InetAddress ip = InetAddress.getLocalHost();
            address = ip.getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Unable to get IP address: " + e.getMessage());
        }
        return address;
    }
}
class Network {
    String subnet = "0.0.0.";
    String[] ipArray = new String[256];

    Network(String Deviceip){
        subnet = Deviceip.substring(0, Deviceip.lastIndexOf(".") + 1);
        for (int i = 0; i <= 255; i++) {
            ipArray[i] = "191.168.8." + i;
        }
    }

    public void scan(int iplevel) {
        for (int i = 0; i <= 255; i++) {
            Ipresult(ipArray[i]);
        }
    }

    void Ipresult(String ipAddress) {
        // ipAddress = "191.168.8.3";
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);

            if (inet.isReachable(5000)) {
                System.out.println("IP address " + ipAddress + " is reachable");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while pinging the IP address: " + e.getMessage());
        }
    }
}