package watchyourstep;

import javax.swing.*;
import java.awt.*;

public class TerrainButton extends JButton {
    private static final int SIZE = 50;

    private int row = 0;
    private int col = 0;
    private int nextToHoles = 0;
    private boolean hole = false;
    private boolean revealed = false;

    public TerrainButton(int row, int col) {
        this.row = row;
        this.col = col;

        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isNextToHoles() {
        return nextToHoles > 0;
    }

    public void increaseHoleCount() {
        this.nextToHoles += 1;
    }

    public boolean hasHole() {
        return hole;
    }

    public void setHole(boolean hasHole) {
        this.hole = hasHole;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void reveal(boolean reveal) {
        this.revealed = reveal;
        if (revealed) {
            if (hasHole()) {
                setBackground(Color.BLACK);
            } else {
                setBackground(Color.CYAN);
                if (isNextToHoles()) {
                    setText("" + nextToHoles);
                }
            }
        } else {
            setBackground(null);
            setText("");
        }
        setFocusPainted(false);
    }

    public void reset() {
        this.hole = false;
        this.revealed = false;
        this.nextToHoles = 0;
        setText("");
        setBackground(null);
    }
}
