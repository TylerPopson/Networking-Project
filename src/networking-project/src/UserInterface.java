import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface extends JFrame {
    private  JPanel drawArea;
    private DrawArea canvas;
    private JPanel controls;
    private JButton connect;
    private JLabel connuctionStatus;
    private JLabel timeLimit;
    private JLabel prompt;
    private JButton submit;
    private JTextField guessTextField;
    private JPanel guessImage;
    private Timer timer;
    private long timeLeft;

    UserInterface(){
        drawArea = new JPanel();
        canvas = new DrawArea();
        controls = new JPanel();
        connect = new JButton("connect");
        connuctionStatus = new JLabel("disconnected");
        timeLimit = new JLabel("0:00");

        timeLeft = 100;
        ActionListener timeAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timeLimit.setText(""+timeLeft);
                timeLimit.repaint();
            }
        };
        timer = new Timer(1000, timeAction);
        timer.start();


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
    void CreateGuessMenu(){
        controls.remove(prompt);
        remove(drawArea);
        guessTextField = new JTextField();
        guessImage = new JPanel();
        guessTextField.setSize(160, 30);
        guessTextField.setLocation(100, 200);
        controls.add(guessTextField);
        add(guessImage);
        //todo set guessImage to image that was received from the sender
        repaint();
    }
}

