import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Representation of a Host.
 * Connects to the statically defined peer in the network.
 * Disconnects.
 */
public class Host {
    private Socket hostSocket; //Peer to reply to.
    private PrintWriter out;
    private BufferedReader in;

    public String sendMessage(String prompt) throws IOException {
        out.println(prompt);
        return in.readLine();
    }

    public void initConnection(String ip, int port) throws IOException {
        hostSocket = new Socket(ip, port); //treat this host as a client for now.
        out = new PrintWriter(hostSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
    }
        public void cutConnection() throws IOException {
            hostSocket.close();
        }

    public static void main(String[] args) throws IOException {
        Host host = new Host();
        host.initConnection("127.0.0.1", 6666);
        //Three-way handshake.
        String response = host.sendMessage("hello peer");
        System.out.println(response);
        //send the terminating char.
        response = host.sendMessage(".");
        System.out.println(response);
        //Tear down the connection.
        host.cutConnection();

        }
    }
