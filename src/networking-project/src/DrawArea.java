import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class DrawArea extends JPanel implements MouseMotionListener {
    int x = -1;
    int y = -1;
    DrawArea(){
        addMouseMotionListener(this);
    }
    public void saveImage(String name,String type) {
        BufferedImage image = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        paint(g2);
        try{
            ImageIO.write(image, type, new File(name+"."+type));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Shape circleShape = new Ellipse2D.Double(x, y, 3, 3);
        g2d.draw(circleShape);
    }
}
