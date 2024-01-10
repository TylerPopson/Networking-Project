import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class MultiServerTest {
    /**
     * Test to verify if server can receive strings.
     * Sends a terminating char "." to end the connection.
     * @throws IOException
     */
    @Test
    public void Client_Send_Img() throws Exception {
        Client client3 = new Client();
        //Specify starting the image service.
        client3.init("127.0.0.1", 4000);
        client3.sendImage();
    }
    @Test
    public void Client_Send_Prompt() throws Exception {

        Client client1 = new Client();
        //Specify starting the image service.
        client1.init("127.0.0.1", 4000);
        //Specify a prompt will be sent.
        String msg = client1.sendMessage("C");
        String msg1 = client1.sendMessage("hello world");
        //Disconnect
//        client1.sendMessage(".");
        client1.init("127.0.0.1", 4000);
        //Message to retrive prompt is not being received by server.
        String prompt = client1.sendMessage("D");
        assertEquals(msg, "Prompt service started");
        assertEquals(prompt, "hello world");
    }
    @Test
    public void Client_Send_Guess() throws Exception {

        Client client1 = new Client();
        //Specify starting the image service.
        client1.init("127.0.0.1", 4000);
        //Specify a prompt will be sent.
        String msg = client1.sendMessage("E");
        String msg1 = client1.sendMessage("teapot");
        //Disconnect
        client1.sendMessage(".");
        client1.init("127.0.0.1", 4000);
        String prompt = client1.sendMessage("F");
        assertEquals(msg, "Guess service started");
        assertEquals(prompt, "teapot");
    }
    @Test
    public void Server_Send_Image() throws Exception {

        Client client4 = new Client();
        //Specify starting the image service.
        client4.init("127.0.0.1", 4000);
//        //Specify an image request.
//        client4.sendImage();
//        //Disconnect
//        client4.init("127.0.0.1", 4000);
        // Retrieve image
        String msg = client4.receiveImage();
        client4.display();
        assertEquals(msg, "Sending image");

    }

}
