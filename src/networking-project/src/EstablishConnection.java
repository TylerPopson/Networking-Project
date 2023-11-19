import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.security.PrivateKey;

/**
 * TODO
 * Establish a static connection with the next host.
 * Displays a greeting when connecting to the next host.
 */
public class EstablishConnection {
    private ServerSocket peerSocket; //Treat other peers as servers for now.
    private Socket hostSocket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;

    public void initConnection(int port) throws IOException {
        peerSocket = new ServerSocket(port);
        hostSocket = peerSocket.accept();
        out = new PrintWriter(hostSocket.getOutputStream(), true); //Get the outputed stream from the connected host.
        in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        String greeting = in.readLine(); // get the initial greeting from the connected host.
        if ("hello world".equals(greeting)) {
            out.println("hello host");
        } else {
            out.println("Unrecognized host");
        }
    }


    /**
     * TODO
     * Accepts a string for the prompt.
     * Sends the prompt to the statically defined next connected host.
     */
    public void sendPrompt(String prompt) {

    }

    /**
     * TODO
     * Closes the connection between the next connected host,
     * displays a message about the closed connection.
     */
    public void cutConnection() throws IOException {
        in.close();
        out.close();
        hostSocket.close();
        peerSocket.close();

    }

}
