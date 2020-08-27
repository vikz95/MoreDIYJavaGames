package watchyourstep;

import mycomponents.TitleLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class WatchYourStep extends JFrame {

    private static final int GRID_SIZE = 10;
    private static final int NUMBER_OF_HOLES = 10;

    private TerrainButton[][] terrain = new TerrainButton[GRID_SIZE][GRID_SIZE];
    private int totalRevealed = 0;

    public WatchYourStep() {
        initGUI();
        setHoles();

        setTitle("Watch Your Step");
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initGUI() {
        TitleLabel titleLabel = new TitleLabel("Watch Your Step");
        add(titleLabel, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        add(centerPanel, BorderLayout.CENTER);
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                terrain[row][col] = new TerrainButton(row, col);
                terrain[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        TerrainButton button = (TerrainButton) e.getSource();
                        int row = button.getRow();
                        int col = button.getCol();
                        clickedTerrain(row, col);
                    }
                });
                centerPanel.add(terrain[row][col]);
            }
        }
    }

    private void setHoles() {
        Random rand = new Random();
        for (int i = 0; i < NUMBER_OF_HOLES; i++) {
            int pickRow = rand.nextInt(GRID_SIZE);
            int pickCol = rand.nextInt(GRID_SIZE);
            while (terrain[pickRow][pickCol].hasHole()) {
                pickRow = rand.nextInt(GRID_SIZE);
                pickCol = rand.nextInt(GRID_SIZE);
            }
            terrain[pickRow][pickCol].setHole(true);
            addToNeighborsHoleCount(pickRow, pickCol);
//            terrain[pickRow][pickCol].reveal(true);
        }
    }

    private void addToNeighborsHoleCount(int row, int col) {
        addToHoleCount(row - 1, col - 1);
        addToHoleCount(row - 1, col);
        addToHoleCount(row - 1, col + 1);
        addToHoleCount(row, col - 1);
        addToHoleCount(row, col + 1);
        addToHoleCount(row + 1, col - 1);
        addToHoleCount(row + 1, col);
        addToHoleCount(row + 1, col + 1);
    }

    private void addToHoleCount(int row, int col) {
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            terrain[row][col].increaseHoleCount();
//            terrain[row][col].reveal(true);
        }
    }

    private void clickedTerrain(int row, int col) {
        if (terrain[row][col].hasHole()) {
            String message = "Game over. Do you want to play again?";
            promptForNewGame(message);
        } else {
            check(row, col);
            checkNeighbors(row, col);
            if (totalRevealed == GRID_SIZE * GRID_SIZE - NUMBER_OF_HOLES) {
                String message = "You won! Do you want to play again?";
                promptForNewGame(message);
            }
        }
    }

    private void check(int row, int col) {
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE
                && !terrain[row][col].hasHole()
                && !terrain[row][col].isRevealed()) {
            terrain[row][col].reveal(true);
            totalRevealed++;
            if (!terrain[row][col].isNextToHoles()) {
                checkNeighbors(row, col);
            }
        }
    }

    private void checkNeighbors(int row, int col) {
        check(row - 1, col - 1);
        check(row - 1, col);
        check(row - 1, col + 1);
        check(row, col - 1);
        check(row, col + 1);
        check(row + 1, col - 1);
        check(row + 1, col);
        check(row + 1, col + 1);
    }

    private void promptForNewGame(String message) {
        showHoles();
        int option = JOptionPane.showConfirmDialog(this, message, "Play again?", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            newGame();
        } else {
            System.exit(0);
        }
    }

    private void showHoles() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (terrain[row][col].hasHole()) {
                    terrain[row][col].reveal(true);
                }
            }
        }
    }

    private void newGame() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                terrain[row][col].reset();
            }
        }
        setHoles();
        totalRevealed = 0;
    }

    public static void main(String[] args) {
        try {
            String className = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WatchYourStep();
            }
        });
    }
}
