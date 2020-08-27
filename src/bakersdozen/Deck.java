package bakersdozen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Deck {

    private static final String[] RANKS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private static final String[] SUIT_SYMBOLS = {"\u2665", "\u2666", "\u2660", "\u2663"};
    private static final int[] VALUES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private static final int CARD_WIDTH = 30;
    private static final int CARD_HEIGHT = 50;
    private static final String FILENAME = "cards.png";

    private ArrayList<Card> cards = new ArrayList<>();

    public Deck() {
        Random rand = new Random();
        try {
            BufferedImage cardsImage = ImageIO.read(new File("src/bakersdozen/" + FILENAME));
            for (int suit = 0; suit < SUIT_SYMBOLS.length; suit++) {
                for (int rank = 0; rank < RANKS.length; rank++) {
                    int pos = 0;
                    if (cards.size() > 0) {
                        pos = rand.nextInt(cards.size() + 1);
                    }
                    int x = rank * CARD_WIDTH;
                    int y = suit * CARD_HEIGHT;
                    Image image = cardsImage.getSubimage(x, y, CARD_WIDTH, CARD_HEIGHT);
                    Card card = new Card(RANKS[rank], suit, VALUES[rank], image);
                    cards.add(pos, card);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not open file " + FILENAME);
        }
    }

    public void copyFrom(Deck deck) {
        cards.clear();
        for (int i = 0; i < deck.size(); i++) {
            cards.add(deck.get(i));
        }
    }

    public Card deal() {
        return cards.remove(0);
    }

    public int size() {
        return cards.size();
    }

    public Card get(int index) {
        return cards.get(index);
    }

    public static String[] getRanks() {
        return RANKS;
    }

    public static String[] getSuitSymbols() {
        return SUIT_SYMBOLS;
    }

    public static int getCardWidth() {
        return CARD_WIDTH;
    }

    public static int getCardHeight() {
        return CARD_HEIGHT;
    }
}
