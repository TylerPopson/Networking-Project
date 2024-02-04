import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
    private MultiServer server;
    private String serverip;
    private Client h;
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
                try {
                    submit();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
        //Start a server.
//        new Thread(new ServerRunnable(4000)).start();

    }

    // start a host connecting to a peer based on the ip typed by the user
    public void connect() throws IOException {
        h = new Client();
        serverip = ipInput.getText();
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
        /**
         * Action listener to capture drawing.
         * Also sends prompt
         */
        ActionListener timeAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (time <= 1) {
                    timer.stop();
                    timerLabel.setText("");
                    timerLabel.repaint();
                    img = canvas.getImage();
                    peerImg = canvas.getImage();

                    try {
                        h.init(serverip, 4000);
                        h.createPlayer();
                        //May send prompt service to a different section behind sending an image.
                        h.init(serverip, 4000);
                        h.sendPrompt(prompt);
                        h.init(serverip, 4000);
                        h.sendImage();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        h.init(serverip, 4000);
                        peerPrompt = h.requestPrompt();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
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

    /**
     * Send guess.
     * Display results.
     *
     * @throws IOException
     */
    public void submit() throws IOException {
        guess = guessInput.getText();
        try {
            h.init(serverip, 4000);
            h.sendGuess(guess);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            display();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void display() throws IOException {
        h.init(serverip,4000);
    peerGuess =h.requestGuess();
    //Display the results here.
        PeerGuessLabel.setText(peerGuess);
        PeerGuessLabel.revalidate();
        PeerGuessLabel.repaint();
    //Make sure this reads the prompt.
    //The peer's prompt is never set.
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
        String[] words = {"Person", "Hat", "Cat", "Piano","Tea","Computer"};
        int rand = ((int) Math.floor(Math.random() * words.length));
        everything = words[rand];
        return everything;
    }

    /**
     * Start the server on a separate thread to ensure no deadlock occurs.
     * Requests to server should go through calling host methods.
     * At the end of program, ensure that the server shuts down.
     */
    public class ServerRunnable implements Runnable{
        int port;
        public ServerRunnable(int port){
            this.port = port;
        }
        public void run(){
            server = new MultiServer();
            try {
                server.start(port);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
}
    public static void main(String[] args) {
        JFrame frame = new JFrame("UI");
        frame.setContentPane(new UI().UI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}