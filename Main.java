import java.util.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;


public class Main {
    public static void main(String[] args) {
        NetworkReceiver myreceiver = new NetworkReceiver(4050);
        myreceiver.start();
        NetworkSender mysender = new NetworkSender();
        IpAddress myIp = new IpAddress();
        String result = myIp.scan();
        if (result.isEmpty()) {
            System.out.println("Systen ip not detected");
        } else {
            System.out.println("My ip address : " + result);
            Network mynetwork = new Network(result);
            mynetwork.start();
            mysender.send();
        }
    }
}

class IpAddress{
    public String scan(){
        String address = "";
        try {
            InetAddress ip = InetAddress.getLocalHost();
            address = ip.getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Unable to get IP address: " + e.getMessage());
        }
        return address;
    }
}
class Network extends Thread{
    String subnet = "0.0.0.";
    String myip = "";
    String[] ipArray = new String[256];
    List<String> DeviceIplist = new ArrayList<String>();

    Network(String Deviceip){
        myip = Deviceip;
        subnet = Deviceip.substring(0, Deviceip.lastIndexOf(".") + 1);
        for (int i = 0; i <= 255; i++) {
            ipArray[i] = "191.168.8." + i;
        }
    }
    @Override
    public void run() {
        for (int i = 0; i <= 255; i++) {
            String filtedIP = Ipresult(ipArray[i]);
            if (!filtedIP.isEmpty()) {
                DeviceIplist.add(filtedIP);
                System.out.println("IP address " + filtedIP + " is reachable");
            }
        }
    }

    String Ipresult(String ipAddress) {
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            if (inet.isReachable(5000)) {
                if(!myip.equals(ipAddress)){
                    return ipAddress;
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while pinging the IP address: " + e.getMessage());
        }
        return "";
    }
}

class NetworkSender {
    public void send() {
        String ipAddress = "191.168.8.3";
        int portNumber = 4050;
        System.out.println("Start to send the message");
        try {
            // Connect to the network
            Socket socket = new Socket(ipAddress, portNumber);

            // Get the output stream
            OutputStream outputStream = socket.getOutputStream();

            // Send the message
            String message = "Hello, network!";
            outputStream.write(message.getBytes());

            // Close the output stream and the socket
            outputStream.close();
            socket.close();

            System.out.println("Message sent to the network.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
class NetworkReceiver extends Thread {
    private int portNumber;

    public NetworkReceiver(int portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public void run() {
        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(portNumber);

            System.out.println("Server Port "+portNumber+" listening for incoming data...");

            while (true) {
                // Listen for incoming connections
                Socket socket = serverSocket.accept();

                // Get the input stream
                InputStream inputStream = socket.getInputStream();

                // Read the message
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);
                String message = new String(buffer, 0, bytesRead);

                // Close the input stream and the socket
                inputStream.close();
                socket.close();

                System.out.println("Received message: " + message);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
