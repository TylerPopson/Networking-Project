import java.io.*;
import java.net.Socket;
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
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public void initConnection(int port) throws Exception {
        peerSocket = new ServerSocket(port);
        hostSocket = peerSocket.accept();
        out = new PrintWriter(hostSocket.getOutputStream(), true); //Get the outputted stream from the connected host.
        in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        String greeting = in.readLine(); // get the initial greeting from the connected host.
        if ("hello peer".equals(greeting)) {
            out.println("hello host");
        } else {
            out.println("Unrecognized greeting");
        }
        dataInputStream = new DataInputStream(
                hostSocket.getInputStream());
        dataOutputStream = new DataOutputStream(
                hostSocket.getOutputStream());
        //Start the session.
        String inputLine;
        receiveFile("test.png");
        while ((inputLine = in.readLine()) != null) {
            if (".".equals(inputLine)) { //termination char.
                out.println("good bye");
                break;
            }
            out.println(inputLine);
        }
    }

    private void receiveFile(String fileName)throws Exception  {
        int bytes = 0;
        FileOutputStream  fileOStream = new FileOutputStream (fileName);
        long size
                = dataInputStream.readLong(); // read file size
        byte[] buffer = new byte[4 * 1024];
        while (size > 0
                && (bytes = dataInputStream.read(
                buffer, 0,
                (int)Math.min(buffer.length, size)))
                != -1) {
            // Here we write the file using write method
            fileOStream.write(buffer, 0, bytes);
            size -= bytes; // read upto file size
        }
        // Here we received file
        System.out.println("File is Received");
        fileOStream.close();
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
    public static void main(String[] args) throws Exception {
        Peer helloHost=new Peer();
        helloHost.initConnection(7777);
        helloHost.receiveFile("test.png");

    }
}
