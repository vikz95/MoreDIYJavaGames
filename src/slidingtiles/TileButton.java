package slidingtiles;

import javax.swing.*;
import java.awt.*;

public class TileButton extends JButton {

    private static int tileSize = 0;
    private static int maxTiles = 0;

    private ImageIcon imageIcon;
    private int imageId = 0;
    private int row = 0;
    private int col = 0;

    public TileButton(ImageIcon imageIcon, int imageId, int row, int col) {
        setImage(imageIcon, imageId);
        this.row = row;
        this.col = col;

        setBackground(Color.WHITE);
        setBorder(null);
        setPreferredSize(new Dimension(tileSize, tileSize));
        setFocusPainted(false);
    }

    public static void setTileSizeAndMaxTiles(int size, int max) {
        tileSize = size;
        maxTiles = max;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public int getImageId() {
        return imageId;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setImage(ImageIcon imageIcon, int imageId) {
        this.imageIcon = imageIcon;
        this.imageId = imageId;

        if (imageId == maxTiles - 1) {
            setIcon(null);
        } else {
            setIcon(imageIcon);
        }
    }

    public boolean hasNoImage() {
        return getIcon() == null;
    }

    public void swap(TileButton otherTile) {
        ImageIcon otherImageIcon = otherTile.getImageIcon();
        int otherImageId = otherTile.getImageId();
        otherTile.setImage(this.imageIcon, this.imageId);
        setImage(otherImageIcon, otherImageId);
    }

    public void showImage() {
        setIcon(this.imageIcon);
    }
}
