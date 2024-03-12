import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
    @Test
    @Order(6)
    public void Client_Request_Results() throws Exception {
        Client client5 = new Client();
        //Specify requesting results.
        client5.init("127.0.0.1", 4000);
        String[]results;
        String[]fResults = new String[4];
        //structured like a do-while loop.
        boolean repeat = false;
        do {
            results = Arrays.copyOf(client5.requestResults(repeat), 4);
            fResults[0] = results[0];
            fResults[1] = results[1];
            fResults[2] = results[2];
            fResults[3] = results[3];
            repeat = true;
        }
        while (!results[0].equals("0"));

        assertEquals(fResults[0], "0");
        assertEquals(fResults[1], "ABC");
        assertEquals(fResults[2], "hello world");
        assertEquals(fResults[3], "teapot");
    }
    public void Client_Request_Img_Results() throws Exception {
        Client client5 = new Client();
        //Specify requesting results.
        client5.init("127.0.0.1", 4000);
        BufferedImage img = client5.requestResultsImg();

    }
    @Test
    @Order(8)
    public void Client_Request_Multiple_Results() throws Exception {
        Client client5 = new Client();
        //Specify requesting results.
        client5.init("127.0.0.1", 4000);
        String[]results;
        String[]fResults = new String[4];
        //Testing for multiple players.
        //Create a data structure for holding the results of all players.
        ArrayList<String[]>pResults = new ArrayList<>();
        //structured like a do-while loop.
        boolean repeat = false;
        do {
            results = Arrays.copyOf(client5.requestResults(repeat), 4);
            pResults.add(Arrays.copyOf(results, 4));
            repeat = true;
        }
        while (!results[0].equals("0"));
        //Test for values of first player.
        fResults = Arrays.copyOf(pResults.getFirst(), 4);
//        assertEquals(fResults[0], "0");
        assertEquals(fResults[1], "ABC");
        assertEquals(fResults[2], "hello world");
        assertEquals(fResults[3], "teapot");
        //If presults's size is greater than 1, perform multiple value testing.
        if (pResults.size() > 1){
            fResults = Arrays.copyOf(pResults.get(1), 4);
//            assertEquals(fResults[0], "0");
            assertEquals(fResults[1], "DEF");
            assertEquals(fResults[2], "blue sky");
            assertEquals(fResults[3], "kettle");
        }
    }
}
