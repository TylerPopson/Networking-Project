import javax.swing.*;

public class ConnectUI {
    private JTextField ipInput;
    private JLabel title;
    private JButton connectButton;
    private JButton cancelButton;
    private JLabel ipLabel;
    private JLabel error;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ConnectUI");
        frame.setContentPane(new ConnectUI().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
