import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.io.IOException;

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

    public String sendMessage(String msg) throws IOException {
        out = new PrintWriter(hostSocket.getOutputStream(), true);
        out.println(msg);
        return in.readLine();
    }

    /**
     * Method used for actually sending the image.
     * Designates an image will be sent.
     * Closes all resources at the end.
     *
     * @throws Exception
     */
    public void sendImage() throws Exception {
        BufferedImage img;
        try {
            //Designate an image is being sent.
            String msg = sendMessage("A");
            System.out.println("Reading image from drive.");
            //Read an image from the drive.
            img = ImageIO.read(new File("C:/Users/Zach's PC/IdeaProjects/Networking-Project/src/networking-project/drawing.png"));
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
    }

    public String receiveImage() throws Exception {
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
        return msg;
    }

    public void display(){
        JFrame f = new JFrame("Server");
        ImageIcon icon = new ImageIcon(bImage);
        JLabel l = new JLabel();

        l.setIcon(icon);
        f.add(l);
        f.pack();
        f.setVisible(true);
    }

    //Specifies the String service should be used.
//    public String startString() throws IOException{
//       return sendMessage("E");
//    }
    public String sendPrompt(String prompt) throws IOException {
        String response = sendMessage("C");
        sendMessage(prompt);
        return response;
    }

    public String receivePrompt() throws IOException {
        return sendMessage("D");
    }

    public String sendGuess(String guess) throws IOException {
        String response = sendMessage("E");
        sendMessage(guess);
        return response;
    }

    public String receiveGuess() throws IOException {
        return sendMessage("F");
    }


    public void init(String ip, int port) throws IOException {
        hostSocket = new Socket(ip, port); //treat this host as a client for now.
        out = new PrintWriter(hostSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
    }

    public void cutConnection() throws IOException {
        hostSocket.close();
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        //Three-way handshake.
        client.init("127.0.0.1", 4000);
        //Tear down the connection.
        client.cutConnection();

    }
}
