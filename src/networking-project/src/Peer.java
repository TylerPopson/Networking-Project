import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import javax.swing.*;
import java.awt.image.*;


/**
 * Representation of a Peer in the network.
 * Listen for connections.
 * Displays a greeting when connected to.
 * Disconnects.
 */
public class Peer {
    private ServerSocket peerSocket; //Treat other peers as servers for now.
    private Socket hostSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String rPrompt;
    private String rGuess;
    private BufferedImage bImage = null;
    private static DataOutputStream dos = null;
    private static DataInputStream dis = null;

    public void initConnection(int port) throws Exception {
        peerSocket = new ServerSocket(port);
        hostSocket = peerSocket.accept();
        out = new PrintWriter(hostSocket.getOutputStream(), true); //Get the outputted stream from the connected host.
        in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        String greeting = in.readLine(); // get the initial greeting from the connected host.
        if ("hello peer".equals(greeting)) {
            out.println("hello host");
        } else {
            out.println("Unrecognized greeting");
        }
        dis = new DataInputStream(
                hostSocket.getInputStream());
        dos = new DataOutputStream(
                hostSocket.getOutputStream());
        //Start the session.
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            //block until an image is received.
            receiveImage3();
            //populate the received prompt and guess in order.
        rPrompt = inputLine;
        rGuess = inputLine;
            if (".".equals(inputLine)) { //termination char.
                out.println("good bye");
                break;
            }
            out.println(inputLine);
        }
    }

//    private void receiveFile(String fileName)throws Exception  {
//        int bytes = 0;
//        FileOutputStream  fileOStream = new FileOutputStream (fileName);
//        long size
//                = dis.readLong(); // read file size
//        byte[] buffer = new byte[4 * 1024];
//        while (size > 0
//                && (bytes = dis.read(
//                buffer, 0,
//                (int)Math.min(buffer.length, size)))
//                != -1) {
//            // Here we write the file using write method
//            fileOStream.write(buffer, 0, bytes);
//            size -= bytes; // read upto file size
//        }
//        // Here we received file
//        System.out.println("File is Received");
//        fileOStream.close();
//    }
//    public void receiveImage()throws FileNotFoundException, IOException, EOFException {
//        int len = dis.readInt(); // causing eof issues.
//        //find image size.
//        byte[] data = new byte[len];
//        dis.readFully(data);
//        InputStream ian = new ByteArrayInputStream(data);
//        BufferedImage bImage = ImageIO.read(ian);
//
//        JFrame f = new JFrame("Server");
//        ImageIcon icon = new ImageIcon(bImage);
//        JLabel l = new JLabel();
//
//        l.setIcon(icon);
//        f.add(l);
//        f.pack();
//        f.setVisible(true);
//
//    }
    public void  receivePrompt() throws Exception{}
    public void receiveImage2() throws Exception{
        InputStream in = hostSocket.getInputStream();
        DataInputStream dis = new DataInputStream(in);

        int len = dis.readInt();
        System.out.println("Image Size: " + len/1024 + "KB");

        byte[] data = new byte[len];
        dis.readFully(data);
        dis.close();
        in.close();

        InputStream ian = new ByteArrayInputStream(data);
        BufferedImage bImage = ImageIO.read(ian);

        JFrame f = new JFrame("Server");
        ImageIcon icon = new ImageIcon(bImage);
        JLabel l = new JLabel();

        l.setIcon(icon);
        f.add(l);
        f.pack();
        f.setVisible(true);
    }
    public void receiveImage3() throws Exception{
        TestPeer peerS = new TestPeer();
        peerS.init();


    }


    /**
     * Accepts a string for the prompt.
     * Sends the prompt to the statically defined next connected host.
     */
    public String sendPrompt(String prompt) {
    return prompt;
    }

    public String getrPrompt(){
        return rPrompt;
    }
    public String getrGuess(){
        return rGuess;
    }
    public BufferedImage getbImage() {
        return bImage;
    }
    /**
     * Closes the connection between the next connected host,
     * displays a message about the closed connection.
     */
    public void cutConnection() throws IOException {
        in.close();
        out.close();
        hostSocket.close();
        peerSocket.close();

    }

    public static void main(String[] args) throws Exception {
        Peer peer = new Peer();
        peer.initConnection(7777);
    }
}
