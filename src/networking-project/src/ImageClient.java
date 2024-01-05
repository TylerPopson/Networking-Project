import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageClient {
    private Socket hostSocket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedImage bImage;

    /**
     * This will be able to send the image and close the connection.
     * Client-Side representation able to send multiple requests to the server.
     * Reads an image from the drive.
     * Sends the image as a stream to the server.
     * Closes the connection.
     */

    public static void main(String[] args) throws Exception {
        ImageClient client = new ImageClient();
        client.init("127.0.0.1", 4000);
    }

    /**
     * Sends a string to the server, used to designate the service needed.
     *
     * @param msg String
     * @return the string sent
     * @throws IOException for sending the string.
     */
    public String sendMessage(String msg) throws IOException {
        out = new PrintWriter(hostSocket.getOutputStream(), true);
        out.println(msg);
        return in.readLine();
    }

    /**
     * Initiates a connection to the server.
     *
     * @param ip   for server address.
     * @param port for server
     * @throws Exception all exceptions
     */
    public void init(String ip, int port) throws Exception {
        hostSocket = new Socket(ip, port);
        System.out.println("Client is running. ");
        in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
    }

    /**
     * Method used for actually sending the string.
     * Designates an image will be sent.
     * Closes all resources at the end.
     *
     * @throws Exception
     */
    public void sendImage() throws Exception {
        BufferedImage img;
        try {
            //Designate an image is being sent.
            String msg = sendMessage("I");
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
    public String receiveImage() throws Exception{
        //Designate an image is being received.
        String msg = sendMessage("L");
        DataInputStream dis = new DataInputStream(hostSocket.getInputStream());
        int len = dis.readInt();
        byte[] data = new byte[len];
        dis.readFully(data);
        dis.close();
        InputStream ian = new ByteArrayInputStream(data);
        bImage = ImageIO.read(ian);
        return msg;
    }
    public void display() throws Exception{
        JFrame f = new JFrame("Server");
        ImageIcon icon = new ImageIcon(bImage);
        JLabel l = new JLabel();

        l.setIcon(icon);
        f.add(l);
        f.pack();
        f.setVisible(true);
    }

}
