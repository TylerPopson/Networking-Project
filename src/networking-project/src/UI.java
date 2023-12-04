import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

public class UI {
    private JFormattedTextField ipInput;
    private JButton connectButton;
    private JPanel UI;
    private JButton submitButton;
    private JLabel ipLabel;
    private JLabel timerLabel;
    private JLabel promptLabel;
    private JPanel displayArea;
    private Timer timer;
    private int time;
    private DrawArea canvas;
    public UI() {
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                connect();
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
        time = 60;

        promptLabel.setText(prompt);
        submitButton.setEnabled(true);
        connectButton.setEnabled(false);

        ActionListener timeAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

    public void connect(){

    }

    public void submit(){
        canvas.saveImage("test", "png");
    }
    public void init(){
        JFrame frame = new JFrame("UI");
        frame.setContentPane(new UI().UI);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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
