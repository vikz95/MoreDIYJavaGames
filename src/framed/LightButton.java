package framed;

import javax.swing.*;
import java.awt.*;

public class LightButton extends JButton {

    private static final int MAXSIZE = 50;

    private int row = 0;
    private int col = 0;

    public LightButton(int row, int col) {
        this.row = row;
        this.col = col;

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(MAXSIZE, MAXSIZE));
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void turnOn() {
        setBackground(Color.RED);
    }

    public void turnOff() {
        setBackground(Color.BLACK);
    }

    public boolean isLit() {
        Color color = getBackground();
        return color.equals(Color.RED);
    }

    public void toggle() {
        if (isLit()) {
            turnOff();
        } else {
            turnOn();
        }
    }
}
