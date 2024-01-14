import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MultiServerTest {
    @Test
    @Order(1)
    public void Client_Create_Player() throws Exception {
        Client client5 = new Client();
        client5.init("127.0.0.1", 4000);
        String msg = client5.createPlayer();
        assertEquals(msg, "Create player service started");
    }
    @Test
    @Order(2)
    public void Client_Send_Img() throws Exception {
        Client client3 = new Client();
        //Specify starting the image service.
        client3.init("127.0.0.1", 4000);
        String msg = client3.sendImage();
        assertEquals(msg, "Image service started");
    }
    @Test
    @Order(3)
    public void Client_Send_Prompt() throws Exception {

        Client client1 = new Client();
        //Specify a prompt will be sent.
        client1.init("127.0.0.1", 4000);
        String msg = client1.sendPrompt("hello world");
        client1.init("127.0.0.1", 4000);
        //Message to retrieve prompt is not being received by server.
        String prompt = client1.requestPrompt();
        assertEquals(msg, "Prompt service started");
        assertEquals(prompt, "hello world");
    }
    @Test
    @Order(4)
    public void Client_Send_Guess() throws Exception {

        Client client1 = new Client();

        client1.init("127.0.0.1", 4000);
        String msg2 = client1.sendGuess("teapot");
        //Disconnect
        client1.sendMessage(".");
        client1.init("127.0.0.1", 4000);
        //causes socket reset.
        String guess = client1.requestGuess();
        assertEquals(msg2, "Guess service started");
        assertEquals(guess, "teapot");
    }
    @Test
    @Order(5)
    public void Client_Request_Image() throws Exception {

        Client client4 = new Client();
        //Specify starting the image service.
        client4.init("127.0.0.1", 4000);
        //causes socket reset.
        String msg2 = client4.requestImage();
        client4.display();
        assertEquals(msg2, "Sending image service started");

    }
}
