package bakersdozen;

import java.awt.*;
import java.util.ArrayList;

public class CardStack {
    ArrayList<Card> cards = new ArrayList<>();
    private int stackX = 0;
    private int stackY = 0;
    private int overlap = 0;

    public CardStack(int stackX, int stackY, int overlap) {
        this.stackX = stackX;
        this.stackY = stackY;
        this.overlap = overlap;
    }

    public void add(Card card) {
        int cardx = stackX;
        int cardy = stackY + overlap * cards.size();
        card.setXY(cardx, cardy);
        cards.add(card);
    }

    public void addToBeginning(Card card) {
        card.setXY(stackX, stackY);
        cards.add(0, card);

        for (int i = 1; i < cards.size(); i++) {
            Card nextCard = cards.get(i);
            nextCard.addToXY(0, overlap);
        }
    }

    public void draw(Graphics g) {
        if (cards.size() > 0 && overlap == 0) {
            int lastIndex = cards.size() - 1;
            Card card = cards.get(lastIndex);
            card.draw(g);
        } else {
            for (Card card : cards) {
                card.draw(g);
            }
        }
    }

    public Card getLast() {
        return cards.get(cards.size() - 1);
    }

    public void removeLast() {
        cards.remove(cards.size() - 1);
    }

    public int size() {
        return cards.size();
    }

    public int getX() {
        return stackX;
    }

    public int getY() {
        return stackY;
    }

    public void clear() {
        cards.clear();
    }
}
