import javax.swing.*;
import java.awt.*;

public class UserInterface extends JFrame {
    private Canvas drawArea;
    private JPanel controls;

    UserInterface(){
        drawArea = new Canvas();
        controls = new JPanel();



        add(drawArea);
        setSize(800, 500);
        setVisible(true);

    }
}
