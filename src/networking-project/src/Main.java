import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        UserInterface ui = new UserInterface();
    EstablishConnection sendingHost=new EstablishConnection();
    sendingHost.initConnection(6666);

    }
}