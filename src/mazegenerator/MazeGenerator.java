package mazegenerator;

import mycomponents.TitleLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class MazeGenerator extends JFrame {
    public static final int TYPE_MAZE = 0;
    public static final int TYPE_ANTIMAZE = 1;
    private int type = TYPE_MAZE;

    private int rows = 25;
    private int cols = 20;
    private Cell[][] cells = new Cell[rows][cols];
    private int currentRow = 0;
    private int currentCol = 0;
    private final int endRow = rows - 1;
    private final int endCol = cols - 1;

    private TitleLabel titleLabel;
    private JPanel mazePanel = new JPanel();

    public MazeGenerator() {
        initGUI();

        setTitle("Maze Generator");
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initGUI() {
        titleLabel = new TitleLabel("Maze");
        add(titleLabel, BorderLayout.PAGE_START);

        // center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLACK);
        add(centerPanel, BorderLayout.CENTER);

        // maze panel
        newMaze();
        centerPanel.add(mazePanel);

        // button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        add(buttonPanel, BorderLayout.PAGE_END);

        JButton newMazeButton = new JButton("New Maze");
        newMazeButton.setFocusable(false);
        newMazeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newMaze();
            }
        });
        buttonPanel.add(newMazeButton);

        JButton optionsButton = new JButton("Options");
        optionsButton.setFocusable(false);
        optionsButton.addActionListener(e -> changeOptions());
        buttonPanel.add(optionsButton);

        // listeners
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                moveBall(keyCode);
            }
        });
    }

    private void newMaze() {
        if (type == TYPE_MAZE) {
            titleLabel.setText("Maze");
        } else {
            titleLabel.setText("Anti-Maze");
        }
        mazePanel.removeAll();
        mazePanel.setLayout(new GridLayout(rows, cols));

        cells = new Cell[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell(r, c, type);
                mazePanel.add(cells[r][c]);
            }
        }
        generateMaze();
        currentRow = 0;
        currentCol = 0;
        cells[currentRow][currentCol].setCurrent(true);
        cells[endRow][endCol].setEnd(true);

        mazePanel.revalidate();
        pack();
    }

    private void generateMaze() {
        ArrayList<Cell> tryLaterCells = new ArrayList<>();
        int totalCells = rows * cols;
        int visitedCells = 1;

        // start at a random cell
        Random rand = new Random();
        int r = rand.nextInt(rows);
        int c = rand.nextInt(cols);

        // while not all cells have yet been visited
        while (visitedCells < totalCells) {
            // find all neighbors with all walls intact
            ArrayList<Cell> neighbors = new ArrayList<>();
            if (isAvailable(r - 1, c)) {
                neighbors.add(cells[r - 1][c]);
            }
            if (isAvailable(r + 1, c)) {
                neighbors.add(cells[r + 1][c]);
            }
            if (isAvailable(r, c - 1)) {
                neighbors.add(cells[r][c - 1]);
            }
            if (isAvailable(r, c + 1)) {
                neighbors.add(cells[r][c + 1]);
            }

            // if one or more was found
            if (neighbors.size() > 0) {
                // if more than one was found, add this cell to the list to try again
                tryLaterCells.add(cells[r][c]);

                // pick a random neighbor and remove the wall
                int pick = rand.nextInt(neighbors.size());
                Cell neighbor = neighbors.get(pick);
                cells[r][c].openTo(neighbor);

                // go to the neighbor and increment the number visited
                r = neighbor.getRow();
                c = neighbor.getCol();
                visitedCells++;
            } else {
                // if none was found, go to one of the cells that was saved to try later
                Cell nextCell = tryLaterCells.remove(0);
                r = nextCell.getRow();
                c = nextCell.getCol();
            }
        }
    }

    private boolean isAvailable(int r, int c) {
        return r >= 0 && r < rows
                && c >= 0 && c < cols
                && cells[r][c].hasAllWalls();
    }

    private void moveBall(int direction) {
        switch (direction) {
            case KeyEvent.VK_UP:
                if (!cells[currentRow][currentCol].isWall(Cell.TOP)) {
                    moveTo(currentRow - 1, currentCol, Cell.TOP, Cell.BOTTOM);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (!cells[currentRow][currentCol].isWall(Cell.BOTTOM)) {
                    moveTo(currentRow + 1, currentCol, Cell.BOTTOM, Cell.TOP);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (!cells[currentRow][currentCol].isWall(Cell.LEFT)) {
                    moveTo(currentRow, currentCol - 1, Cell.LEFT, Cell.RIGHT);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (!cells[currentRow][currentCol].isWall(Cell.RIGHT)) {
                    moveTo(currentRow, currentCol + 1, Cell.RIGHT, Cell.LEFT);
                }
                break;
        }
        if (currentRow == endRow && currentCol == endCol) {
            JOptionPane.showMessageDialog(this, "Congratulations! You solved it.");
        }
    }

    private void moveTo(int nextRow, int nextCol, int firstDirection, int secondDirection) {
        cells[currentRow][currentCol].setCurrent(false);
        cells[currentRow][currentCol].addPath(firstDirection);
        currentRow = nextRow;
        currentCol = nextCol;
        cells[nextRow][nextCol].setCurrent(true);
        cells[nextRow][nextCol].addPath(secondDirection);
    }

    private void changeOptions() {
        OptionsDialog dialog = new OptionsDialog(rows, cols, type);
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        if (!dialog.isCanceled()) {
            rows = dialog.getRows();
            cols = dialog.getCols();
            type = dialog.getMazeType();
            newMaze();
        }
    }

    public static void main(String[] args) {
        try {
            String className = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(() -> new MazeGenerator());
    }
}
