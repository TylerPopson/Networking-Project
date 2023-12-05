import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UI {
    private JFormattedTextField ipInput;
    private JButton connectButton;
    private JPanel UI;
    private JButton submitButton;
    private JLabel ipLabel;
    private JLabel timerLabel;
    private JLabel promptLabel;
    private JPanel displayArea;
    private JTextField guessInput;
    private Timer timer;
    private int time;
    private DrawArea canvas;
    private Peer p;
    private Host h;
    private String pronpt;
    private String peerPrompt;
    private  String guess;
    private String peerGuess;
    public UI() {
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    connect();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                play();
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                submit();

            }
        });

        
    }

    public void play(){

        canvas = new DrawArea();
        canvas.setSize(new Dimension(500, 500));
        String prompt = getWord();
        time = 20;

        promptLabel.setText(prompt);
        //submitButton.setEnabled(true);
        connectButton.setEnabled(false);

        ActionListener timeAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(time <= 1){
                    timer.stop();
                    //TODO
                    //send image, and prompt
                    canvas.saveImage("drawing", "png");
                    try {
                        h.sendMessage(prompt);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    displayArea.removeAll();
                    displayArea.revalidate();
                    displayArea.repaint();
                    guess();
                }
                time--;
                timerLabel.setText(""+time);
                timerLabel.repaint();
            }
        };
        timer = new Timer(1000, timeAction);
        timer.start();
        displayArea.add(canvas);
        displayArea.revalidate();
        displayArea.validate();
    }

    public void connect() throws IOException {
        h = new Host();
        h.initConnection(ipInput.getText(), 6666);
    }

    public void submit(){
        guess = guessInput.getText();

        try {
            h.sendMessage(guess);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //p.sendPrompt();
    }
    public void init() throws IOException {
        JFrame frame = new JFrame("UI");
        frame.setContentPane(new UI().UI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        p = new Peer();
        p.initConnection(6666);
    }

    public void guess(){

        try {
            BufferedImage img = ImageIO.read(new File("drawing.png"));
            ImageIcon icon = new ImageIcon(img);
            JLabel label = new JLabel(icon);
            displayArea.add(label);
        } catch (IOException e) {
           throw new RuntimeException(e);
        }

//        try {
//            Desktop.getDesktop().open(new File("test.png" ));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        guessInput.setEnabled(true);
        submitButton.setEnabled(true);

    }

    public String getWord(){
        String everything = "";
        int rand = ((int) Math.floor(Math.random() * 6775));
        try(BufferedReader br = new BufferedReader(new FileReader("./src/nounlist.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            int i = 0;

            while (line != null) {
                if (i == rand) {
                    sb.append(line);
                }
                i++;
                line = br.readLine();
            }
            everything = sb.toString();
        }catch (Exception e){
            System.out.println(e.toString());
        }

        return  everything;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("UI");
        frame.setContentPane(new UI().UI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
