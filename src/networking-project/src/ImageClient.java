import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
public class ImageClient {
    private Socket soc;
    private PrintWriter out;
    private BufferedReader in;
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
    public String sendMessage(String msg) throws IOException {
        out = new PrintWriter(soc.getOutputStream(), true);
        out.println(msg);
        return in.readLine();
    }
    /**
     * @param ip for server address.
     * @param port for server
     * @throws Exception all exceptions
     */
    public void init(String ip, int port) throws Exception{
        BufferedImage img;
        soc = new Socket(ip, port);
        System.out.println("Client is running. ");
        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));

        try {
            sendMessage("I");
            System.out.println("Reading image from drive.");
            img = ImageIO.read(new File("C:/Users/Zach's PC/IdeaProjects/Networking-Project/src/networking-project/drawing.png"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(img, "jpg", baos);
            baos.flush();

            byte[] bytes = baos.toByteArray();
            baos.close();

            System.out.println("Sending image to server. ");

            OutputStream out = soc.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            dos.writeInt(bytes.length);
            dos.write(bytes, 0, bytes.length);

            System.out.println("Image sent to server. ");

            dos.close();
            out.close();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            soc.close();
        }
        soc.close();
    }

}
