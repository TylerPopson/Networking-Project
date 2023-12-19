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
public class TestPeer {
    private static BufferedImage bImage = null;
    //This will be able to accept the image, although the connection will be closed.

    public static void main(String  args[]) throws Exception {
        TestPeer testPeer = new TestPeer();
        testPeer.init();
    }
    public void init() throws Exception {
        ServerSocket server = null;
        Socket socket;
        server = new ServerSocket(4000);
        System.out.println("Server Waiting for image");
        //client socket is the first connection request.
        socket = server.accept();
        System.out.println("Client connected.");
        //begin processing the stream.
        InputStream in = socket.getInputStream();
        DataInputStream dis = new DataInputStream(in);

        int len = dis.readInt();
        System.out.println("Image Size: " + len / 1024 + "KB");

        byte[] data = new byte[len];
        dis.readFully(data);
        dis.close();
        in.close();

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
    //return the buffered image.
    public BufferedImage getbImage(){
        return bImage;
    }

}