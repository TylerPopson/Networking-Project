import java.io.*;
import java.net.Socket;

/**
 * Representation of a Host.
 * Connects to the statically defined peer in the network.
 * Disconnects.
 */
public class Host {
    private Socket hostSocket; //Peer to reply to.
    private PrintWriter out;
    private BufferedReader in;
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public String sendMessage(String prompt) throws IOException {
        out.println(prompt);
        return in.readLine();
    }
    void sendFile(String path) throws FileNotFoundException, IOException {
    int bytes = 0;
    //open the file with designated path.
        File file = new File(path);
        FileInputStream fileIStream = new FileInputStream(file);

        //send file to server
        dataOutputStream.writeLong(file.length());
        //Buffer file
        byte[] buffer = new byte [4 * 1024];
        while ((bytes = fileIStream.read(buffer))
                != -1){
            //send file over socket.
            dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush();
        }
        // close the file here
        fileIStream.close();
    }


    public void initConnection(String ip, int port) throws IOException {
        hostSocket = new Socket(ip, port); //treat this host as a client for now.
        out = new PrintWriter(hostSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        //Get stream for file transfer.
        dataInputStream = new DataInputStream (hostSocket.getInputStream());
        dataOutputStream = new DataOutputStream(hostSocket.getOutputStream());
    }
        public void cutConnection() throws IOException {
            hostSocket.close();
        }

    public static void main(String[] args) throws IOException {
        Host host = new Host();
        host.initConnection("127.0.0.1", 7777);
        //Three-way handshake.
        String response = host.sendMessage("hello peer");
        System.out.println(response);
        //send the terminating char.
        host.sendFile("C:/Users/Zach/IdeaProjects/Networking-Project/src/networking-project/test.png");
        response = host.sendMessage(".");
        System.out.println(response);

        //Tear down the connection.
        host.cutConnection();

        }
    }
