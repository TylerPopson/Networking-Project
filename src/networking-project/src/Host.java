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
/**
 * Representation of a Host.
 * Connects to the statically defined peer in the network.
 * Disconnects.
 */
public class Host {
    private Socket hostSocket; //Peer to reply to.
    private PrintWriter out;
    private BufferedReader in;
    private BufferedImage img;
    private static DataOutputStream dos = null;
    private static DataInputStream dis = null;

    public String sendMessage(String prompt) throws IOException {
        out = new PrintWriter(hostSocket.getOutputStream(), true);
        out.println(prompt);
        String a = in.readLine();
        return a;
    }
//    void sendFile(String path) throws FileNotFoundException, IOException {
//    int bytes = 0;
//    //open the file with designated path.
//        File file = new File(path);
//        FileInputStream fileIStream = new FileInputStream(file);
//
//        //send file to server
//        dos.writeLong(file.length());
//        //Buffer file
//        byte[] buffer = new byte [4 * 1024];
//        while ((bytes = fileIStream.read(buffer))
//                != -1){
//            //send file over socket.
//            dos.write(buffer, 0, bytes);
//            dos.flush();
//        }
//        // close the file here
//        fileIStream.close();
//    }
//    void sendImage (String path) throws FileNotFoundException, IOException {
//        System.out.println("Reading image from disk. ");
//        img = ImageIO.read(new File(path));
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        ImageIO.write(img, "png", baos);
//        baos.flush();
//
//        byte[] bytes = baos.toByteArray();
//        baos.close();
//        //Send the image.
//        dos.writeInt(bytes.length);
//        dos.write(bytes, 0, bytes.length);
//        //close the output.
//        System.out.println("Image sent");
//    }
    //will need to take a buffered image as a paramater.
//    public void sendImage2 (String path) throws Exception{
//        try {
//            System.out.println("Reading image from disk.");
//            img = ImageIO.read(new File(path));
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            ImageIO.write(img, "jpg", baos);
//            baos.flush();
//
//            byte[] bytes = baos.toByteArray();
//            baos.close();
//
//            System.out.println("Sending image to server. ");
//
//            OutputStream out = hostSocket.getOutputStream();
//            DataOutputStream dos = new DataOutputStream(out);
//
//            dos.writeInt(bytes.length);
//            dos.write(bytes, 0, bytes.length);
//
//            System.out.println("Image sent to server. ");
//
////            dos.close();
////            out.close();
//
//        } catch (Exception e) {
//            System.out.println("Exception: " + e.getMessage());
//            hostSocket.close();
//        }
//    }
    public void sendImage3 (String path) throws Exception{
        TestHost hostS = new TestHost();
        hostS.init();
    }


    public void initConnection(String ip, int port) throws IOException {
        hostSocket = new Socket(ip, port); //treat this host as a client for now.
        out = new PrintWriter(hostSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        //Get stream for file transfer.
        dis = new DataInputStream (hostSocket.getInputStream());
        dos = new DataOutputStream(hostSocket.getOutputStream());
    }
        public void cutConnection() throws IOException {
            hostSocket.close();
        }

    public static void main(String[] args) throws Exception {
        Host host = new Host();
        host.initConnection("127.0.0.1", 7777);
        //Three-way handshake.
        String response = host.sendMessage("hello peer");
        System.out.println(response);
        host.sendImage3("C:/Users/Zach/IdeaProjects/Networking-Project/src/networking-project/test.png");
        //send the terminating char.
        response = host.sendMessage(".");
        System.out.println(response);

        //Tear down the connection.
        host.cutConnection();

        }
    }
