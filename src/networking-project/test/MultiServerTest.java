import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class MultiServerTest {
    /**
     * Test to verify if server can receive strings.
     * Sends a terminating char "." to end the connection.
     * @throws Exception
     */
    @Test
    @Order(1)
    public void Client_Create_Player() throws Exception {
        Client client5 = new Client();
        client5.init("127.0.0.1", 4000);
        //Specify starting the code service.
        // Retrieve image
        String msg = client5.createPlayer();
        assertEquals(msg, "Create player service started");
    }
    @Test
    @Order(2)
    public void Client_Send_Img() throws Exception {
        Client client3 = new Client();
        //Specify starting the image service.
        client3.init("127.0.0.1", 4000);
        client3.sendImage();
    }
    @Test
    @Order(3)
    public void Client_Send_Prompt() throws Exception {

        Client client1 = new Client();
        //Specify starting the image service.
        client1.init("127.0.0.1", 4000);
        client1.createPlayer();
        //Specify a prompt will be sent.
        String msg = client1.sendMessage("C");
        client1.sendMessage("hello world");
        //Disconnect
//        client1.sendMessage(".");
        client1.init("127.0.0.1", 4000);
        //Message to retrive prompt is not being received by server.
        String prompt = client1.sendMessage("D");
        assertEquals(msg, "Prompt service started");
        assertEquals(prompt, "hello world");
    }
    @Test
    @Order(4)
    public void Client_Send_Guess() throws Exception {

        Client client1 = new Client();

        client1.init("127.0.0.1", 4000);
        //Specify a prompt will be sent.
        String msg2 = client1.sendMessage("E");
        String msg3 = client1.sendMessage("teapot");
        //Disconnect
        client1.sendMessage(".");
        client1.init("127.0.0.1", 4000);
        String prompt = client1.sendMessage("F");
        assertEquals(msg2, "Guess service started");
        assertEquals(prompt, "teapot");
    }
    @Test
    @Order(5)
    public void Server_Send_Image() throws Exception {

        Client client4 = new Client();
        //Specify starting the image service.
        client4.init("127.0.0.1", 4000);
//        //Specify an image request.
//        client4.sendImage();
//        //Disconnect
//        client4.init("127.0.0.1", 4000);
        // Retrieve image
        String msg2 = client4.receiveImage();
        client4.display();
        assertEquals(msg2, "Sending image");

    }
    @Test
    @Order(6)
    public void Client_Send_Code() throws Exception {
    }
}
