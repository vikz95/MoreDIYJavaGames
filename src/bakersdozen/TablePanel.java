package bakersdozen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class TablePanel extends JPanel {

    private static final int CARD_WIDTH = Deck.getCardWidth();
    private static final int CARD_HEIGHT = Deck.getCardHeight();
    private static final int SPACING = 4;
    private static final int MARGIN = 10;
    private static final int WIDTH = 13 * CARD_WIDTH + 12 * SPACING + 2 * MARGIN;
    private static final int HEIGHT = 9 * CARD_HEIGHT + 3 * MARGIN;
    private static final int FOUNDATION_X = WIDTH / 2 - (4 * CARD_WIDTH + 3 * SPACING) / 2;
    private static final int FOUNDATION_Y = MARGIN;
    private static final int BOARD_X = MARGIN;
    private static final int BOARD_Y = CARD_HEIGHT + MARGIN + MARGIN;
    private static final int OVERLAP = (int) (CARD_HEIGHT * .65);

    private Deck deck;
    private Deck savedDeck = new Deck();
    private CardStack[] foundations = new CardStack[4];
    private CardStack[] columns = new CardStack[13];

    private Card movingCard;
    private int mouseX = 0;
    private int mouseY = 0;
    private int fromCol = 0;

    public TablePanel() {
        int x = FOUNDATION_X;
        int y = FOUNDATION_Y;
        for (int i = 0; i < foundations.length; i++) {
            foundations[i] = new CardStack(x, y, 0);
            x += CARD_WIDTH + SPACING;
        }

        x = BOARD_X;
        y = BOARD_Y;
        for (int i = 0; i < columns.length; i++) {
            columns[i] = new CardStack(x, y, OVERLAP);
            x += CARD_WIDTH + SPACING;
        }

        newGame();

        // mouse listeners
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clicked(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                released(e.getX(), e.getY());
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                dragged(e.getX(), e.getY());
            }
        });
    }

    private void deal() {
        // clear the foundations and columns
        for (CardStack foundation : foundations) {
            foundation.clear();
        }
        for (CardStack column : columns) {
            column.clear();
        }
        // deal 4 rows of 13 cards
        for (int row = 0; row < 4; row++) {
            for (CardStack column : columns) {
                Card card = deck.deal();
                if (card.getValue() == 12) {
                    column.addToBeginning(card);
                } else {
                    column.add(card);
                }
            }
        }
        repaint();
    }

    private void clicked(int x, int y) {
        movingCard = null;
        for (int col = 0; col < columns.length && movingCard == null; col++) {
            if (columns[col].size() > 0) {
                Card card = columns[col].getLast();
                if (card.contains(x, y)) {
                    movingCard = card;
                    mouseX = x;
                    mouseY = y;
                    columns[col].removeLast();
                    fromCol = col;
                }
            }
        }
    }

    private void dragged(int x, int y) {
        if (movingCard != null) {
            int changeX = x - mouseX;
            int changeY = y - mouseY;
            movingCard.addToXY(changeX, changeY);
            mouseX = x;
            mouseY = y;
            repaint();
        }
    }

    private void released(int x, int y) {
        if (movingCard != null) {
            boolean validMove = false;

            // play on a foundation?
            for (int i = 0; i < foundations.length && !validMove; i++) {
                int foundationX = foundations[i].getX();
                int foundationY = foundations[i].getY();
                if (movingCard.isNear(foundationX, foundationY)) {
                    // empty foundation?
                    if (foundations[i].size() == 0) {
                        if (movingCard.getValue() == 0) {
                            validMove = true;
                            foundations[i].add(movingCard);
                            movingCard = null;
                        }
                    }
                    // otherwise, valid suit and rank?
                    else {
                        Card topCard = foundations[i].getLast();
                        if (movingCard.getSuit() == topCard.getSuit()
                                && movingCard.getValue() == topCard.getValue() + 1) {
                            validMove = true;
                            foundations[i].add(movingCard);
                            movingCard = null;
                            isGameOver();
                        }
                    }
                }
            }
            // play on a column?
            for (int i = 0; i < columns.length && !validMove; i++) {
                if (columns[i].size() > 0) {
                    Card card = columns[i].getLast();
                    if (movingCard.isNear(card)
                            && movingCard.getValue() == card.getValue() - 1) {
                        validMove = true;
                        columns[i].add(movingCard);
                        movingCard = null;
                    }
                }
            }
            // otherwise return to column
            if (!validMove) {
                columns[fromCol].add(movingCard);
                movingCard = null;
            }
            repaint();
        }
    }

    private void isGameOver() {
        boolean gameOver = true;
        for (int i = 0; i < foundations.length && gameOver; i++) {
            if (foundations[i].size() != 13) {
                gameOver = false;
            }
        }
        if (gameOver) {
            String message = "Do you want to play again?";
            int option = JOptionPane.showConfirmDialog(this, message, "Play again?", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                newGame();
            } else {
                System.exit(0);
            }
        }
    }

    public void newGame() {
        deck = new Deck();
        savedDeck.copyFrom(deck);
        deal();
    }

    public void replay() {
        deck.copyFrom(savedDeck);
        deal();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // draw background
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // draw foundation
        for (CardStack foundation : foundations) {
            if (foundation.size() > 0) {
                foundation.draw(g);
            } else {
                Card.drawOutline(g, foundation.getX(), foundation.getY());
            }
        }

        // draw board
        for (CardStack column : columns) {
            column.draw(g);
        }

        // draw moving card
        if (movingCard != null) {
            movingCard.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
