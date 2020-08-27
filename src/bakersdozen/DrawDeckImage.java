package bakersdozen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawDeckImage {


    public static void main(String[] args) {
        String[] suits = Deck.getSuitSymbols();
        String[] ranks = Deck.getRanks();
        int cardWidth = Deck.getCardWidth();
        int cardHeight = Deck.getCardHeight();

        int imageWidth = 13 * cardWidth;
        int imageHeight = 4 * cardHeight;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, imageWidth, imageHeight);

        Font font = new Font(Font.DIALOG, Font.BOLD, 24);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);

        for (int row = 0, y = 0; row < 4; row++, y += cardHeight) {
            for (int col = 0, x = 0; col < 13; col++, x += cardWidth) {
                g.setColor(Color.WHITE);
                g.fillRoundRect(x, y, cardWidth - 1, cardHeight - 1, 8, 8);
                g.setColor(Color.BLACK);
                g.drawRoundRect(x, y, cardWidth - 1, cardHeight - 1, 8, 8);

                if (row < 2) {
                    g.setColor(Color.RED);
                }
                String rank = ranks[col];
                int rankWidth = fm.stringWidth(rank);
                int fromLeft = x + cardWidth / 2 - rankWidth / 2;
                int fromTop = y + 20;
                g.drawString(rank, fromLeft, fromTop);

                String suit = suits[row];
                int suitWidth = fm.stringWidth(suit);
                fromLeft = x + cardWidth / 2 - suitWidth / 2;
                fromTop = y + 45;
                g.drawString(suit, fromLeft, fromTop);
            }
        }

        String fileName = "cards.png";
        File file = new File("src/bakersdozen/" + fileName);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not save " + fileName);
        }
    }
}

