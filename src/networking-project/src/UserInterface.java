import javax.swing.*;
import java.awt.*;

public class UserInterface extends JFrame {
    private  JPanel drawArea;
    private DrawArea canvas;
    private JPanel controls;
    private JButton connect;
    private JLabel connuctionStatus;
    private JLabel timeLimit;
    private JLabel prompt;
    private JButton submit;

    UserInterface(){
        drawArea = new JPanel();
        canvas = new DrawArea();
        controls = new JPanel();
        connect = new JButton("connect");
        connuctionStatus = new JLabel("disconnected");
        timeLimit = new JLabel("0:00");
        prompt = new JLabel("Prompt");
        submit = new JButton("submit");

        canvas.setBackground(new Color(0.6f,0.6f,0.6f));
        canvas.setSize(500, 500);

        drawArea.add(canvas);
        drawArea.setBackground(new Color(10));
        drawArea.setLayout(null);

//      Configure the controls and info panel
        controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
        controls.setSize(300, 500);

        timeLimit.setFont(new Font("Fira Code", Font.BOLD, 24));
        prompt.setFont(new Font("Fira Code", Font.ITALIC, 24));

        controls.add(Box.createRigidArea(new Dimension(200, 10)));
        controls.add(connect);
        controls.add(connuctionStatus);
        controls.add(Box.createRigidArea(new Dimension(0, 80)));
        controls.add(timeLimit);
        controls.add(Box.createRigidArea(new Dimension(0, 20)));
        controls.add(prompt);
        controls.add(Box.createRigidArea(new Dimension(0, 80)));
        controls.add(submit);

//      configure the main window
        setLayout(new BorderLayout(10, 5));

        add(drawArea, BorderLayout.CENTER);
        add(controls, BorderLayout.EAST);

        setSize(new Dimension(800, 500));
        setVisible(true);

    }
}
