import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionUI extends JFrame{
    private JPanel fields;
    private JButton connect;
    private JButton cancel;
    private JLabel error;
    private  JTextField ip;

    ConnectionUI(){
        fields = new JPanel();
        connect = new JButton();
        cancel = new JButton();
        error = new JLabel("");

        ip = new JTextField();
        ip.setMaximumSize(new Dimension(200, 35));
        ip.setAlignmentX(0.5f);

        connect.setAlignmentX(0.5f);
        fields.setLayout(new BoxLayout(fields, BoxLayout.PAGE_AXIS));
        fields.setSize(new Dimension(30, 30));
        fields.add(error);
        fields.add(ip);
        fields.add(connect);
        fields.add(cancel);




        add(fields);
        setSize(300, 300);
        setVisible(true);
    }

}
