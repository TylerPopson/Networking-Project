import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Results extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel DrawingPane;
    private JPanel PeerDrawingPane;
    private JLabel PromptLabel;
    private JLabel PeerGuessLabel;
    private JLabel PeerPromptLabel;
    private JLabel GuessLabel;

    public Results() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public void init(){
        Results dialog = new Results();


        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void update(String prompt, String guess, String pPrompt, String pGuess, BufferedImage img, BufferedImage pImg){
        PromptLabel.setText(prompt);
        PromptLabel.repaint();

        PeerPromptLabel.setText(pPrompt);
        PeerPromptLabel.repaint();

        GuessLabel.setText(guess);
        GuessLabel.repaint();

        PeerGuessLabel.setText(pGuess);
        PeerGuessLabel.repaint();

        ImageIcon drawingicon = new ImageIcon(img);
        JLabel drawing = new JLabel(drawingicon);
        DrawingPane.add(drawing);

        ImageIcon peerDrawingIcon = new ImageIcon(pImg);
        JLabel peerDrawing = new JLabel(peerDrawingIcon);
        PeerDrawingPane.add(peerDrawing);

        DrawingPane.revalidate();
        PeerDrawingPane.revalidate();
        repaint();
    };

    public static void main(String[] args) {
        Results dialog = new Results();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
