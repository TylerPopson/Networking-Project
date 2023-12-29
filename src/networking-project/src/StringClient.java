import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Representation of a Host.
 * Connects to the statically defined peer in the network.
 * Disconnects.
 */
public class StringClient {
    private Socket hostSocket; //Peer to reply to.
    private PrintWriter out;
    private BufferedReader in;

    public String sendMessage(String msg) throws IOException {
        out = new PrintWriter(hostSocket.getOutputStream(), true);
        out.println(msg);
        String a = in.readLine();
        return a;
    }
    //Specifies the String service should be used.
//    public String startString() throws IOException{
//       return sendMessage("E");
//    }

    public void initConnection(String ip, int port) throws IOException {
        hostSocket = new Socket(ip, port); //treat this host as a client for now.
        out = new PrintWriter(hostSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
    }
        public void cutConnection() throws IOException {
            hostSocket.close();
        }

    public static void main(String[] args) throws Exception {
        StringClient stringClient = new StringClient();
        //Three-way handshake.
        stringClient.initConnection("127.0.0.1", 4000);
        //Specify strings will be sent.
//        stringClient.startString();
        String response = stringClient.sendMessage("hello host");
        System.out.println(response);
        response = stringClient.sendMessage("stop copying me");
        System.out.println(response);
        //send the terminating char.
        response = stringClient.sendMessage(".");
        System.out.println(response);
        //Tear down the connection.
        stringClient.cutConnection();

        }
    }
