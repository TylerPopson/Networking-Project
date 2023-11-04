import javax.swing.*;
import java.awt.*;

public class UserInterface extends JFrame {
    private  JPanel drawArea;
    private Canvas canvas;
    private JPanel controls;
    private JButton connect;
    private JLabel connuctionStatus;
    private JLabel timeLimit;
    private JLabel prompt;
    private JButton submit;

    UserInterface(){
        drawArea = new JPanel();
        canvas = new Canvas();
        controls = new JPanel();
        connect = new JButton("connect");
        connuctionStatus = new JLabel("disconnected");
        timeLimit = new JLabel("0:00");
        prompt = new JLabel("Prompt");
        submit = new JButton("submit");

        canvas.setBackground(new Color(0.6f,0.6f,0.6f));
        canvas.setSize(new Dimension(500, 500));

        drawArea.add(canvas);

//      Configure the controls and info pannel
        controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
        controls.setSize(300, 500);

        controls.add(connect);
        controls.add(connuctionStatus);
        controls.add(timeLimit);
        controls.add(prompt);
        controls.add(submit);

//      configure the main window
        setLayout(new BorderLayout(10, 5));

        add(drawArea, BorderLayout.CENTER);
        add(controls, BorderLayout.EAST);

        setSize(new Dimension(800, 500));
        setVisible(true);

    }
}
