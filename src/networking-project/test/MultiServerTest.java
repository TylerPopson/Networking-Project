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
    }

}
