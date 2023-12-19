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
//
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
        String response = host.sendMessage("hello host");
        System.out.println(response);
        response = host.sendMessage("stop copying me");
        System.out.println(response);
        //host.sendImage3("C:/Users/Zach/IdeaProjects/Networking-Project/src/networking-project/test.png");
        //send the terminating char.
        response = host.sendMessage(".");
        System.out.println(response);
        //Tear down the connection.
        host.cutConnection();

        }
    }
