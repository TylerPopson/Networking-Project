import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;

/**
 * Handles multiple clients
 * Creates a socket for every client
 * Uses "." char to terminate connection.
 * TODO
 * Implement handling for image sent.
 */
public class MultiServer {
    //Server representation
    private ServerSocket serverSocket;
    //Buffered Image for holding image.
    private BufferedImage bImage = null;
    public static void main(String  args[]) throws Exception {
        MultiServer server = new MultiServer();
        server.start(7777);
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //Accept a connection.
        while (true)
            //Handle an echo request.
            new EchoClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    /**
     * Handler for an echo request
     * Uses threads
     */
    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);

            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
                out.println(inputLine);
            }
            in.close();
            out.close();
            clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static class ImageClientHandler extends Thread {

    }
}
