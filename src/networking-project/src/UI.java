import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
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
    private JLabel PeerPromptLabel;
    private JLabel PeerGuessLabel;
    private JLabel peerPromptLabel;
    private JLabel peerGuessLabel;
    private Timer timer;
    private int time;
    private DrawArea canvas;
    private Peer p;
    private Host h;
    private String prompt;
    private String peerPrompt;
    private String guess;
    private String peerGuess;
    private BufferedImage img;
    private BufferedImage peerImg;

    // add event listeners for button presses
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

    //create the ui and start up peer connection
    public void init() throws IOException {
        JFrame frame = new JFrame("UI");
        frame.setContentPane(new UI().UI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        p = new Peer();
        try {
            p.initConnection(6666);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // start a host connecting to a peer based on the ip typed by the user
    public void connect() throws IOException {
        h = new Host();
        h.initConnection(ipInput.getText(), 6666);
    }

    // start the gameplay
    // gets a prompt, starts a timer, and makes a canvas to draw on
    // play ends when the timer = 0
    public void play() {
        canvas = new DrawArea();
        canvas.setSize(new Dimension(500, 500));
        prompt = getWord();
        time = 20;
        promptLabel.setText(prompt);
        connectButton.setEnabled(false);

        ActionListener timeAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (time <= 1) {
                    timer.stop();
                    timerLabel.setText("");
                    timerLabel.repaint();
                    img = canvas.getImage();
                    //TODO
                    //send image

                    // change to recieved image later
                    peerImg = canvas.getImage();

                    try {
                        h.sendMessage(prompt);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    peerPrompt = "TODO";
                    displayArea.removeAll();
                    displayArea.revalidate();
                    displayArea.repaint();
                    promptLabel.setText("");
                    promptLabel.repaint();
                    guess();
                }
                time--;
                timerLabel.setText("" + time);
                timerLabel.repaint();
            }
        };
        timer = new Timer(1000, timeAction);
        timer.start();
        displayArea.add(canvas);
        displayArea.revalidate();
        displayArea.validate();
    }

    // submit guess of image
    public void submit() {
        guess = guessInput.getText();
        try {
            h.sendMessage(guess);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // TODO
        // get guess
        peerGuess = "TODO";

        PeerGuessLabel.setText(peerGuess);
        PeerGuessLabel.revalidate();
        PeerGuessLabel.repaint();

        PeerPromptLabel.setText(peerPrompt);
        PeerPromptLabel.repaint();

        timerLabel.setText(prompt);
        timerLabel.repaint();

        promptLabel.setText(guess);
    }

    // set the form up for guessing what the drawing is
    // loads other users drawing
    // replaces the canvas with it ahd enables the guess field
    public void guess() {
        ImageIcon icon = new ImageIcon(img);
        JLabel label = new JLabel(icon);
        displayArea.add(label);
        guessInput.setEnabled(true);
        submitButton.setEnabled(true);

    }

    // helper function that loads a file of words and picks a random one to use for the prompt
    public String getWord() {
        String everything = "";
        int rand = ((int) Math.floor(Math.random() * 6775));
        try (BufferedReader br = new BufferedReader(new FileReader("./src/nounlist.txt"))) {
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
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return everything;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("UI");
        frame.setContentPane(new UI().UI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}