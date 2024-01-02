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
     */
    private ServerSocket serverSocket;
    //Buffered Image for holding image.
    private static BufferedImage bImage = null;
    private static String prompt;
    private String guess;


    public static void main(String[] args) throws Exception {
        MultiServer server = new MultiServer();
        server.start(4000);
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //Accept a connection.
        while (true)
            //Handle an image request.
            //new ImageClientHandler(serverSocket.accept()).start();
            //Handle an echo request.
            new ControlClientHandler(serverSocket.accept()).start();
    }

    /**
     * Method implementation of the echo feature.
     * Used by the echo client handler.
     */
    public static void EchoHandler(OutputStream outs, InputStream ins) throws IOException {
        PrintWriter out = new PrintWriter(outs, true);
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
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
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    public static void ImageHandler(OutputStream outs, InputStream ins) throws IOException {
        //begin processing the stream.
        DataInputStream dis = new DataInputStream(ins);
        int len = dis.readInt();
        System.out.println("Image Size: " + len / 1024 + "KB");

        //deallocate resources.
        byte[] data = new byte[len];
        dis.readFully(data);
        dis.close();

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

    }

    public BufferedImage getbImage() {
        return bImage;
    }

    public String getGuess() {
        return guess;
    }

    public static String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }


    /**
     * Accepts a new connection and blocks until service is specified.
     * creates another thread based on the service needed.
     * Services are determined based on chars.
     * Ends after assigning thread.
     */
    private class ControlClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        //constructor
        public ControlClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                //control section, block for a character specifying service needed.
                while ((inputLine = in.readLine()) != null) {
                    switch(inputLine){
                        case "S":
                            out.println("String service started");
                            EchoHandler(clientSocket.getOutputStream(), clientSocket.getInputStream());
                            break;
                        case "P":
                            out.println("Prompt service started");
                            setPrompt(in.readLine());
                            break;
                            //Private value is removed upon end of thread.
                        case "I":
                            out.println("Image service started");
                            ImageHandler(clientSocket.getOutputStream(), clientSocket.getInputStream());
                            break;
                        case "K":
                            out.println(getPrompt());
                            break;
                        default:
                            out.println("Session terminated");
                            break;
                    }
                    break;
                }
                //close resources just in case.
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
