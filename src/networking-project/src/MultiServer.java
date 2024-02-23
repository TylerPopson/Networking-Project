import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;


public class MultiServer {
    /**
     * Server-Side representation.
     * Handles multiple clients.
     * Handles clients reconnecting.
     * Creates a socket for every client.
     */
    private ServerSocket serverSocket;
    //Buffered Image for holding image.
    private static final AtomicReference<BufferedImage> bImage = new AtomicReference<>();
    private static final AtomicReference<String> prompt = new AtomicReference<>();
    private final AtomicReference<String> guess = new AtomicReference<>();
    private final AtomicReference<Integer>playercount = new AtomicReference<>(0);
    //Array representation of connected players.
    private final Player[] source = new Player[]{new Player(), new Player()};
    private final AtomicReferenceArray<Player> cPlayers = new AtomicReferenceArray<>(source);
    private class Player {
        private Player(){}
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
    public void rImageHandler(InputStream ins, String code) throws IOException {
        //begin processing the stream.
        DataInputStream dis = new DataInputStream(ins);
        int len = dis.readInt();
        //deallocate resources.
        byte[] data = new byte[len];
        dis.readFully(data);
        dis.close();
        InputStream ian = new ByteArrayInputStream(data);
        //Set the drawn image.
        setbImage(ImageIO.read(ian), code);
    }

    //Handles requests for images from client.
    public void sImageHandler(OutputStream outs, InputStream ins, String code) throws Exception {
        Thread.sleep(1000);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage img = getbImage(code);
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
    //Handles setting and getting images.
    public BufferedImage getbImage(String code) {
        return  getCurrentPlayer(code).getpImage();
    }

    public void setbImage(BufferedImage img, String code) {
        getCurrentPlayer(code).setpImage(img);

    }
    //Handles setting and getting a clients prompt
    public String getPrompt(String code) {
        return  getCurrentPlayer(code).getPrompt();
    }

    public void setPrompt(String q, String code) {
        getCurrentPlayer(code).setPrompt(q);
    }
    //Handles setting and getting a clients guess
    public String getGuess(String code) {
        return getCurrentPlayer(code).getGuess();
    }

    public void setGuess(String g, String code) {
        getCurrentPlayer(code).setGuess(g);
    }

    /**
     * Creates and initializes a player object with a specified code.
     * Adds the player to the data structure holding player information.
     * Increments the player count.
     * @param code player code
     */
    public void CreatePlayer(String code){
        Player player1 = new Player(code);
        cPlayers.set(playercount.get(), player1);
        playercount.set(playercount.get()+1);
    }

    /**
     * Compares the specified code with each player in the data structure holding player information.
     * @param code player code
     * @return the current player. Returns null if no player with the specified code is found.
     */
    public Player getCurrentPlayer(String code){
        Player currentPlayer;
        currentPlayer = cPlayers.get(0);
        if (Objects.equals(currentPlayer.code, code)){
            return currentPlayer;
        }
        currentPlayer = cPlayers.get(1);
        if (Objects.equals(currentPlayer.code, code)){
            return currentPlayer;
        }
        return null;
    }
    public void sendResults(OutputStream outs, InputStream ins) {
        PrintWriter out = new PrintWriter(outs, true);
        //may want to decrement.
        for (int i = playercount.get(); i >= 0; i--){
        Player currentPlayer = cPlayers.get(i);
        out.println(String.valueOf(i));
        out.println(currentPlayer.getCode());
        out.println(currentPlayer.getPrompt());
        out.println(currentPlayer.getGuess());
    }
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
        ThreadLocal<String> code = new ThreadLocal<>();

        //constructor
        public ControlClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        //May assign code and publish player with code later.

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine = in.readLine();
                //Specify the player being modified by their code.
                code.set(inputLine);
                out.println("player" + code.get());
                //control section, block for a character specifying service needed.
                while ((inputLine = in.readLine()) != null) {
                    switch (inputLine) {
                        case "A":
                            out.println("Image service started");
                            rImageHandler(clientSocket.getInputStream(), code.get());
                            break;
                        case "B":
                            out.println("Sending image service started");
                            sImageHandler(clientSocket.getOutputStream(), clientSocket.getInputStream(), code.get());
                            break;
                        case "C":
                            out.println("Prompt service started");
                            setPrompt(in.readLine(), code.get());
                            break;
                        case "D":
                            out.println(getPrompt(code.get()));

                            break;
                        case "E":
                            out.println("Guess service started");
                            setGuess(in.readLine(), code.get());
                            break;
                        case "F":
                            out.println(getGuess(code.get()));
                            break;
                        case "G":
                            out.println("Create player service started");
                            CreatePlayer(code.get());
                            break;
                        case "H":
                            out.println("Player results service started");
                            sendResults(clientSocket.getOutputStream(), clientSocket.getInputStream());
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
