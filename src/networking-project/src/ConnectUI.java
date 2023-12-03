import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectUI {
    private JTextField ipInput;
    private JLabel title;
    private JButton connectButton;
    private JButton cancelButton;
    private JLabel ipLabel;
    private JLabel error;
    private JPanel ConnectUI;

    public ConnectUI() {
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boolean connectionError = false;

                //attempt connect

                if (connectionError){
                    error.setVisible(true);
                }else {
                    // start game and close connection window
                    
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //close program
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ConnectUI");
        frame.setContentPane(new ConnectUI().ConnectUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
