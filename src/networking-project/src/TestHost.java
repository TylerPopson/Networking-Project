import javax.swing.*;
import java.net.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class TestHost {
    //This will be able to send the image and close the connection.
    public static void main(String args[]) throws Exception {
    }
    public void init() throws Exception{
        Socket soc;
        BufferedImage img = null;
        soc=new Socket("192.168.0.11",4000);
        System.out.println("Client is running. ");

        try {
            System.out.println("Reading image from disk.");
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
