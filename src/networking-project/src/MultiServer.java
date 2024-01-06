import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicReference;


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
    private static final AtomicReference<BufferedImage> bImage = new AtomicReference<>();
    private static final AtomicReference<String> prompt = new AtomicReference<String>();
    private final AtomicReference<String> guess = new AtomicReference<String>();
    //Create a queue for sharing data between threads.
    SynchronousQueue<String> queue = new SynchronousQueue<>();

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
        //Take any variables in the SynQueue
        {
            new ControlClientHandler(serverSocket.accept()).start();
        }


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

    public static void ImageHandler(InputStream ins) throws IOException {
        //begin processing the stream.
        DataInputStream dis = new DataInputStream(ins);
        int len = dis.readInt();
        //System.out.println("Image Size: " + len / 1024 + "KB");

        //deallocate resources.
        byte[] data = new byte[len];
        dis.readFully(data);
        dis.close();

        InputStream ian = new ByteArrayInputStream(data);
        bImage.set(ImageIO.read(ian));

        //displays the image.
//        JFrame f = new JFrame("Server");
//        ImageIcon icon = new ImageIcon(bImage);
//        JLabel l = new JLabel();
//
//        l.setIcon(icon);
//        f.add(l);
//        f.pack();
//        f.setVisible(true);

    }

    public void sendImageHandler(OutputStream outs, InputStream ins) throws Exception {
        Thread.sleep(1000);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage img = getbImage();
        try {
            ImageIO.write(img, "jpg", baos);
            baos.flush();

            byte[] bytes = baos.toByteArray();
            baos.close();
            //Send image to server.
            System.out.println("Sending image to client. ");

            DataOutputStream dos = new DataOutputStream(outs);

            dos.writeInt(bytes.length);
            dos.write(bytes, 0, bytes.length);
            System.out.println("Image sent to client. ");
            //Close stream.
            dos.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public BufferedImage getbImage() {
        return bImage.get();
    }

    public String getGuess() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return guess.toString();
    }

    public static String getPrompt() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return prompt.toString();
    }

    public void setPrompt(String q) {
        prompt.set(q);
    }

    public void setGuess(String g) {

        guess.set(g);
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
                //Does not accept all messages currently.
                while ((inputLine = in.readLine()) != null) {
                    switch (inputLine) {
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
                            ImageHandler(clientSocket.getInputStream());
                            break;
                        case "K":
                            out.println(getPrompt());
                            break;
                        case "L":
                            out.println("Sending image");
                            sendImageHandler(clientSocket.getOutputStream(), clientSocket.getInputStream());
                            break;
                        case "M":
                            out.println("Guess service started");
                            setGuess(in.readLine());
                            break;
                        case "N":
                            out.println(getGuess());
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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
