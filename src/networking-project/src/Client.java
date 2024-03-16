import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Representation of a Host.
 * Connects to the statically defined peer in the network.
 * Disconnects.
 */
public class Client {
    private Socket hostSocket; //Peer to reply to.
    private PrintWriter out;
    private BufferedReader in;
    private BufferedImage bImage;
    private int playercount;
    Player player = new Player("ABC");

    public class Player {
        public Player(){}
        public Player(String code) {
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

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        return in.readLine();
    }

    public void init(String ip, int port) throws IOException {
        hostSocket = new Socket(ip, port); //treat this host as a client for now.
        out = new PrintWriter(hostSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        sendMessage(player.getCode());

    }

    public String createPlayer() throws Exception {
        return sendMessage("G");
    }

    public String sendImage() throws Exception {
        BufferedImage img;
        String msg = "";
        try {
            //Designate an image is being sent.
            msg = sendMessage("A");
            System.out.println("Reading image from drive.");
            //Read an image from the drive.
            img = ImageIO.read(new File("lines.png"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(img, "jpg", baos);
            baos.flush();

            byte[] bytes = baos.toByteArray();
            baos.close();
            //Send image to server.
            System.out.println("Sending image to server. ");

            OutputStream out = hostSocket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            dos.writeInt(bytes.length);
            dos.write(bytes, 0, bytes.length);

            System.out.println("Image sent to server. ");
            //Close stream.
            dos.close();
            out.close();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            hostSocket.close();
        }
        //Ensure that connection is closed.
        hostSocket.close();
        return msg;
    }

    public String requestImage() throws Exception {
        //Designate an image is being received.
        //send Message gets the image stream instead of the next value.
        String msg = sendMessage("B");
        DataInputStream dis = new DataInputStream(hostSocket.getInputStream());
        int len = dis.readInt();
        byte[] data = new byte[len];
        dis.readFully(data);
        dis.close();
        InputStream ian = new ByteArrayInputStream(data);
        bImage = ImageIO.read(ian);
        display(bImage);
        return msg;
    }

    public String sendPrompt(String prompt) throws IOException {
        String response = sendMessage("C");
        sendMessage(prompt);
        return response;
    }

    public String requestPrompt() throws IOException {
        return sendMessage("D");
    }

    public String sendGuess(String guess) throws IOException {
        String response = sendMessage("E");
        sendMessage(guess);
        return response;
    }

    public String requestGuess() throws IOException {
        return sendMessage("F");
    }
    public int requestPlayerCount() throws IOException{
        return Integer.parseInt(sendMessage("J"));
    }
    public void cutConnection() throws IOException {
        hostSocket.close();
    }
    public void display(BufferedImage img) {
        JFrame f = new JFrame("Server");
        ImageIcon icon = new ImageIcon(img);
        JLabel l = new JLabel();
        l.setIcon(icon);
        f.add(l);
        f.pack();
        f.setVisible(true);

    }

    /**
     * @return A players drawn image.
     * @throws Exception
     */

    public BufferedImage requestResultsImg(Boolean repeatCall) throws Exception {
        if (!repeatCall) {
            String response = sendMessage("I");
        }
        DataInputStream dis = new DataInputStream(hostSocket.getInputStream());
        int len = dis.readInt();
        System.out.println("len is " + len);
        byte[] data = new byte[len];
        dis.readFully(data);
        InputStream ian = new ByteArrayInputStream(data);
        return ImageIO.read(ian);
    }
    public String[] requestResults(Boolean repeatCall) throws Exception {
        String[] results;
        if (!repeatCall) {
            String response = sendMessage("H");
        }
        String left = in.readLine();
        String code = in.readLine();
        String prompt = in.readLine();
        String guess = in.readLine();
    return results = new String[]{left, code, prompt, guess};
    }
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        //Three-way handshake.
        client.init("127.0.0.1", 4000);
        //Tear down the connection.
        client.cutConnection();
        client.requestImage();

    }
}
