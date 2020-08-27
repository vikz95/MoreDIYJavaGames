package imageresizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private ImageResizer imageResizer = null;
    private BufferedImage image = null;
    private int cropX = 0;
    private int cropY = 0;
    private int cropW = 0;
    private int cropH = 0;
    private int startX = 0;
    private int startY = 0;

    public ImagePanel(ImageResizer imageResizer) {
        this.imageResizer = imageResizer;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                imageClicked(x, y);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                cropDragged(x, y);
            }
        });
    }

    private void imageClicked(int x, int y) {
        startX = x;
        startY = y;
        cropX = x;
        cropY = y;
        imageResizer.setCropFields(cropX, cropY, cropW, cropH);
        repaint();
    }

    private void cropDragged(int x, int y) {
        if (x > startX) {
            cropW = x - startX;
        } else {
            cropW = startX - x;
            cropX = x;
        }
        if (y > startY) {
            cropH = y - startY;
        } else {
            cropH = startY - y;
            cropY = y;
        }
        imageResizer.setCropFields(cropX, cropY, cropW, cropH);
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        if (image == null) {
            return new Dimension(640, 480);
        } else {
            return new Dimension(image.getWidth(), image.getHeight());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, 0, 0, null); // this or null?
        g.setColor(Color.BLACK);
        g.setXORMode(Color.WHITE);
        g.drawRect(cropX, cropY, cropW, cropH);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
        revalidate();
    }

    public void setCropX(int cropX) {
        this.cropX = cropX;
        repaint();
    }

    public void setCropY(int cropY) {
        this.cropY = cropY;
        repaint();
    }

    public void setCropW(int cropW) {
        this.cropW = cropW;
        repaint();
    }

    public void setCropH(int cropH) {
        this.cropH = cropH;
        repaint();
    }

    public void resetCrop() {
        cropX = 0;
        cropY = 0;
        cropW = 0;
        cropH = 0;
        repaint();
    }
}
