import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI {
    private JFormattedTextField ipInput;
    private JButton connectButton;
    private JPanel UI;
    private JButton submitButton;
    private JLabel ipLabel;
    private JLabel timerLabel;
    private JLabel promptLabel;

    public UI() {
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("UI");
        frame.setContentPane(new UI().UI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
