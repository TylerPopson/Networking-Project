import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;


public class MultiServer {
    /**
     * Server-Side representation.
     * Handles multiple clients.
     * Handles clients reconnecting.
     * Creates a socket for every client.
     * Uses "." char to terminate connection.
     * Accepts a request to process and store an image.
     * TODO
     * Allow multiple requests.
     */
    private ServerSocket serverSocket;
    //Buffered Image for holding image.
    public static void main(String  args[]) throws Exception {
        MultiServer server = new MultiServer();
        server.start(4000);
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //Accept a connection.
        while (true)
            //Handle an image request.
            new ImageClientHandler(serverSocket.accept()).start();
            //Handle an echo request.
            //new EchoClientHandler(serverSocket.accept()).start();
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
        private BufferedImage bImage = null;
        private Socket clientSocket;

        public ImageClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                //begin processing the stream.
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                int len = dis.readInt();
                System.out.println("Image Size: " + len / 1024 + "KB");

                //deallocate resources.
                byte[] data = new byte[len];
                dis.readFully(data);
                dis.close();
                clientSocket.close();

                InputStream ian = new ByteArrayInputStream(data);
                bImage = ImageIO.read(ian);

                //displays the image.
                JFrame f = new JFrame("Server");
                ImageIcon icon = new ImageIcon(bImage);
                JLabel l = new JLabel();

                l.setIcon(icon);
                f.add(l);
                f.pack();
                f.setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        public BufferedImage getbImage() {
            return bImage;
        }
    }
}
