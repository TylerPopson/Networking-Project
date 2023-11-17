import java.net.*;

public class EstablishConnection {
    private ServerSocket peerSocket; //Treat other peers as servers for now.
    private Socket hostSocket;
    private int port;
    public void iniConnection(){
        peerSocket = new ServerSocket(port);
        hostSocket = peerSocket.accept();
    }

    /**
     * TODO
     * Accepts a string for the prompt.
     * Sends the prompt to the next connected host.
     */
    public void sendPrompt(String prompt){}

    public void cutConnection(){
    }

}
