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
    private BufferedImage image;
    DrawArea(){
        addMouseMotionListener(this);
        this.setPreferredSize(new Dimension(500, 500));
        image = new BufferedImage(this.getPreferredSize().width, this.getPreferredSize().height, BufferedImage.TYPE_INT_RGB);
    }
    public void saveImage(String name,String type) {

        try{
            ImageIO.write(image, type, new File(name+"."+type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        x = mouseEvent.getX();
        y = mouseEvent.getY();
        repaint();
        Graphics2D g2 = image.createGraphics();
        paint(g2);

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
