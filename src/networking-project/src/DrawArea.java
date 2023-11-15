import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class DrawArea extends JPanel implements MouseMotionListener {
    int x = -1;
    int y = -1;
    DrawArea(){
        addMouseMotionListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        x = mouseEvent.getX();
        y = mouseEvent.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    public void paintComponent(Graphics g){
        g.fillOval(x,y,5,5);
    }
}
