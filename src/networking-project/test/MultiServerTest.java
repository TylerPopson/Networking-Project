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
    public void Client_Send_Msg_Then_Terminate() throws IOException {
        StringClient client1 = new StringClient();
        client1.init("127.0.0.1", 4000);
        //Specify starting the string service.
        String msg = client1.sendMessage("S");
        String msg1 = client1.sendMessage("hello");
        String msg2 = client1.sendMessage("world");
        String terminate = client1.sendMessage(".");

        assertEquals(msg1, "hello");
        assertEquals(msg2, "world");
        assertEquals(terminate, "bye");
    }

    /**
     * Second copy of client.
     * Verifies if server can handle multiple connections.
     * @throws IOException
     */
    @Test
    public void Client2_Send_Msg_Then_Terminate() throws IOException {
        StringClient client2 = new StringClient();
        //Specify starting the string service.
        client2.init("127.0.0.1", 4000);
        String msg = client2.sendMessage("S");
        String msg1 = client2.sendMessage("hello");
        String msg2 = client2.sendMessage("world");
        String terminate = client2.sendMessage(".");

        assertEquals(msg1, "hello");
        assertEquals(msg2, "world");
        assertEquals(terminate, "bye");
    }
    @Test
    public void Client_Send_Msg_Using_Control() throws IOException {
        StringClient client1 = new StringClient();
        client1.init("127.0.0.1", 4000);
        //Specify starting the string service.
        String msg = client1.sendMessage("S");
        String msg1 = client1.sendMessage("hello");
        String msg2 = client1.sendMessage("world");
        String terminate = client1.sendMessage(".");

        assertEquals(msg1, "hello");
        assertEquals(msg2, "world");
        assertEquals(terminate, "bye");
    }
    @Test
    public void Client_Send_Img() throws Exception {
        ImageClient client3 = new ImageClient();
        //Specify starting the image service.
        client3.init("127.0.0.1", 4000);
        client3.sendImage();
    }
    @Test
    public void Client_Send_Prompt() throws Exception {

        StringClient client1 = new StringClient();
        //Specify starting the image service.
        client1.init("127.0.0.1", 4000);
        //Specify a prompt will be sent.
        String msg = client1.sendMessage("P");
        String msg1 = client1.sendMessage("hello world");
        //Disconnect
        client1.sendMessage(".");
        client1.init("127.0.0.1", 4000);
        //Message to retrive prompt is not being received by server.
        String prompt = client1.sendMessage("K");
        assertEquals(msg, "Prompt service started");
        assertEquals(prompt, "hello world");
    }
    @Test
    public void Client_Send_Image() throws Exception {

        ImageClient client4 = new ImageClient();
        //Specify starting the image service.
        client4.init("127.0.0.1", 4000);
        //Specify an image will be sent.
        client4.sendImage();
        //Disconnect
        client4.init("127.0.0.1", 4000);
        // Retrieve image
        String msg = client4.receiveImage();
        client4.display();
        assertEquals(msg, "Image service started");

    }
}
