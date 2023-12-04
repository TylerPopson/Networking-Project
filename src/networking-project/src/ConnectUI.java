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
    private Connection con;

    public ConnectUI() {
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("clicked");
                boolean connectionError = false;

                //attempt connect
                Host host = new Host();
                Peer peer = new Peer();



                if (connectionError){
                    error.setVisible(true);
                }else {
                    // start game and close connection window
                    con = new Connection(host, peer);
                    System.out.println("connected");
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });


    }

    public void init(){
        JFrame frame = new JFrame("ConnectUI");
        frame.setContentPane(new ConnectUI().ConnectUI);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Connection getCon() {
        return con;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ConnectUI");
        frame.setContentPane(new ConnectUI().ConnectUI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
