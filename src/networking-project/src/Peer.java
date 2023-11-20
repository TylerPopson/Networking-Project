import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;


/**
 * Representation of a Peer in the network.
 * Listen for connections.
 * Displays a greeting when connected to.
 * Disconnects.
 */
public class Peer {
    private ServerSocket peerSocket; //Treat other peers as servers for now.
    private Socket hostSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void initConnection(int port) throws IOException {
        peerSocket = new ServerSocket(port);
        hostSocket = peerSocket.accept();
        out = new PrintWriter(hostSocket.getOutputStream(), true); //Get the outputed stream from the connected host.
        in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        String greeting = in.readLine(); // get the initial greeting from the connected host.
        if ("hello peer".equals(greeting)) {
            out.println("hello host");
        } else {
            out.println("Unrecognized greeting");
        }
    }


    /**
     * Accepts a string for the prompt.
     * Sends the prompt to the statically defined next connected host.
     */
    public String sendPrompt(String prompt) {
    return prompt;
    }

    /**
     * Closes the connection between the next connected host,
     * displays a message about the closed connection.
     */
    public void cutConnection() throws IOException {
        in.close();
        out.close();
        hostSocket.close();
        peerSocket.close();

    }
    public static void main(String[] args) throws IOException {
        Peer helloHost=new Peer();
        helloHost.initConnection(6666);
    }
}
