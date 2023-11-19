import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerReply {
    private Socket hostSocket; //Peer to reply to.
    public void startReply(String ip, int port) throws IOException {
        clientSocket = new Socket (ip, port); //treat the connecting host as a client for now.

    }
    public void iniConnection(int port) throws IOException {
        peerSocket = new ServerSocket(port);
        hostSocket = peerSocket.accept();
    }
}
