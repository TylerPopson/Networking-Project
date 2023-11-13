import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class DrawArea extends JPanel implements MouseMotionListener {
    DrawArea(){
        addMouseMotionListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        System.out.println(mouseEvent.getX() + ", " + mouseEvent.getY());
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
