import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        MultiServer server = new MultiServer();
        server.start(4000);
    }
}