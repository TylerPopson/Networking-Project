import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.util.Objects;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;


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
    private final AtomicReference<Integer>playercount = new AtomicReference<Integer>();
    //Create a queue for sharing data between threads.
    //Array representation of connected players.
    private final AtomicReference<String>code = new AtomicReference<>();
    //Make sure to specify code
    private final AtomicReference<Player> player1 = new AtomicReference<>();
//    private Player[]source = new Player[2];
//    //Atomic data structure is created from source array.
//    private final AtomicReferenceArray<Player> cPlayers = new AtomicReferenceArray<Player>(source);
    SynchronousQueue<String> queue = new SynchronousQueue<>();
    private class Player {
        private Player(String code){
            this.code = code;
        }
        private String prompt;
        private String guess;
        private BufferedImage pImage;
        private String code;

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        public String getGuess() {
            return guess;
        }

        public void setGuess(String guess) {
            this.guess = guess;
        }

        public BufferedImage getpImage() {
            return pImage;
        }

        public void setpImage(BufferedImage pImage) {
            this.pImage = pImage;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
    public static void main(String[] args) throws Exception {
        MultiServer server = new MultiServer();
        server.start(4000);
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        //Accept a connection.
        while (true)
        //Handle all requests within a new thread.
        {
            new ControlClientHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
    //Handles receiving images from the client.

    public void rImageHandler(InputStream ins) throws IOException {
        //begin processing the stream.
        DataInputStream dis = new DataInputStream(ins);
        int len = dis.readInt();
        //deallocate resources.
        byte[] data = new byte[len];
        dis.readFully(data);
        dis.close();
        InputStream ian = new ByteArrayInputStream(data);
        //Set the drawn image.
        setbImage(ImageIO.read(ian));
    }

    public void sImageHandler(OutputStream outs, InputStream ins) throws Exception {
        Thread.sleep(1000);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage img = player1.get().getpImage();
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
        return  player1.get().getpImage();
//                bImage.get();
    }

    public void setbImage(BufferedImage img) {
        player1.get().setpImage(img);
//        bImage.set(img);
    }

    public String getPrompt() {
//        try {
////            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return  player1.get().getPrompt();

//                prompt.toString();
    }

    public void setPrompt(String q) {
        player1.get().setPrompt(q);
//        prompt.set(q);

    }

    public String getGuess() {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return player1.get().getGuess();
//        guess.toString();
    }

    public void setGuess(String g) {
        player1.get().setGuess(g);
    }
    public void CreatePlayer(String code){

//            cPlayers.set(playercount.get(), new Player(code));
            player1.get().setCode(code);
    playercount.set(playercount.get()+1);
    }
//    public Player getCurrentPlayer(String code){
//        if (Objects.equals(cPlayers.get(0).getCode(), code)){
//            return cPlayers.get(0);
//        }
//        else if (Objects.equals(cPlayers.get(1).getCode(), code))
//            return cPlayers.get(1);
//        return null;
//    }
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
                        case "A":
                            out.println("Image service started");
                            rImageHandler(clientSocket.getInputStream());
                            break;
                        case "B":
                            out.println("Sending image");
                            sImageHandler(clientSocket.getOutputStream(), clientSocket.getInputStream());
                            break;
                        case "C":
                            out.println("Prompt service started");
                            setPrompt(in.readLine());
                            break;
                        case "D":
                            out.println(getPrompt());
                            break;
                        case "E":
                            out.println("Guess service started");
                            setGuess(in.readLine());
                            break;
                        case "F":
                            out.println(getGuess());
                            break;
                        case "G":
                            out.println("Create player service started");
                            CreatePlayer(in.readLine());
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
