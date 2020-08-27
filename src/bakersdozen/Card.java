package bakersdozen;

import java.awt.*;

public class Card {

    private static int width = 0;
    private static int height = 0;

    private String rank = "";
    private int suit = -1;
    private int value = 0;
    private Image image = null;
    private int x = 0;
    private int y = 0;

    public Card(String rank, int suit, int value, Image image) {
        this.rank = rank;
        this.suit = suit;
        this.value = value;
        this.image = image;
        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public static void drawOutline(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, width, height, 8, 8);
    }

    public boolean contains(int pointX, int pointY) {
        return pointX >= x
                && pointX <= x + width
                && pointY >= y
                && pointY <= y + height;
    }

    public boolean isNear(Card card) {
        return isNear(card.getX(), card.getY());
    }

    public boolean isNear(int pointX, int pointY) {
        int offsetX = width / 2;
        int offsetY = height;
        return pointX > x - offsetX
                && pointX < x + offsetX
                && pointY > y - offsetY
                && pointY < y + offsetY;
    }

    public String getRank() {
        return rank;
    }

    public int getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addToXY(int x, int y) {
        this.x += x;
        this.y += y;
    }
}
