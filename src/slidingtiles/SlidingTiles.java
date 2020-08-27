package slidingtiles;

import mycomponents.TitleLabel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class SlidingTiles extends JFrame {

    private static final String FILENAME = "slidingTilesImage.jpg";
    private static final int IMAGESIZE = 200;
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

    private int tileSize = 50;
    private int gridSize = 4;
    private BufferedImage image = null;
    private TileButton[][] tiles = new TileButton[gridSize][gridSize];
    private JPanel centerPanel = new JPanel();

    public SlidingTiles() {
        try {
            image = ImageIO.read(new File(FILENAME));
            TileButton.setTileSizeAndMaxTiles(tileSize, gridSize * gridSize);
            initGUI();

            setTitle("Sliding Tiles");
            setResizable(false);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        } catch (IOException e) {
            String message = "The image file " + FILENAME + " could not be opened.";
            JOptionPane.showMessageDialog(this, message);
        }
    }

    private void initGUI() {
        // menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e -> open());
        fileMenu.add(openMenuItem);

        JMenu sizeMenu = new JMenu("Size");
        menuBar.add(sizeMenu);
        JMenuItem size3MenuItem = new JMenuItem("3 x 3");
        size3MenuItem.addActionListener(e -> setGridSize(3));
        sizeMenu.add(size3MenuItem);
        JMenuItem size4MenuItem = new JMenuItem("4 x 4");
        size4MenuItem.addActionListener(e -> setGridSize(4));
        sizeMenu.add(size4MenuItem);
        JMenuItem size5MenuItem = new JMenuItem("5 x 5");
        size5MenuItem.addActionListener(e -> setGridSize(5));
        sizeMenu.add(size5MenuItem);

        // title
        TitleLabel titleLabel = new TitleLabel("Sliding Tiles");
        add(titleLabel, BorderLayout.PAGE_START);

        // main panel
        divideImage();

        // button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        add(buttonPanel, BorderLayout.PAGE_END);

        JButton scrambleButton = new JButton("Scramble");
        scrambleButton.addActionListener(e -> newGame());
        buttonPanel.add(scrambleButton);
    }

    private void open() {
        JFileChooser fileChooser = new JFileChooser();
        ImageFileFilter fileFilter = new ImageFileFilter();
        fileChooser.setFileFilter(fileFilter);
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage newImage = ImageIO.read(file);
                int width = newImage.getWidth();
                int height = newImage.getHeight();
                if (width < height) {
                    height = width;
                } else {
                    width = height;
                }
                Graphics g = image.getGraphics();
                g.drawImage(newImage, 0, 0, IMAGESIZE, IMAGESIZE, 0, 0, width, height, this);
                g.dispose();
                divideImage();
            } catch (IOException e) {
                String message = "The image file " + file.getPath() + " could not be opened.";
                JOptionPane.showMessageDialog(this, message);
            }
        }
    }

    private void setGridSize(int size) {
        gridSize = size;
        tileSize = IMAGESIZE / gridSize;
        TileButton.setTileSizeAndMaxTiles(tileSize, gridSize * gridSize);
        tiles = new TileButton[gridSize][gridSize];
        divideImage();
        pack();
    }

    private void divideImage() {
        centerPanel.setLayout(new GridLayout(gridSize, gridSize));
        add(centerPanel, BorderLayout.CENTER);
        centerPanel.removeAll();

        int imageId = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int x = col * tileSize;
                int y = row * tileSize;
                BufferedImage subImage = image.getSubimage(x, y, tileSize, tileSize);
                ImageIcon imageIcon = new ImageIcon(subImage);
                tiles[row][col] = new TileButton(imageIcon, imageId, row, col);
                tiles[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        TileButton button = (TileButton) e.getSource();
                        clickedTile(button);
                    }
                });
                centerPanel.add(tiles[row][col]);
                imageId++;
            }
        }
        centerPanel.revalidate();
        scramble();
    }

    private void clickedTile(TileButton clickedTile) {
        int row = clickedTile.getRow();
        int col = clickedTile.getCol();

        if (row > 0 && tiles[row - 1][col].hasNoImage()) {
            clickedTile.swap(tiles[row - 1][col]);
        } else if (row < gridSize - 1 && tiles[row + 1][col].hasNoImage()) {
            clickedTile.swap(tiles[row + 1][col]);
        } else if (col > 0 && tiles[row][col - 1].hasNoImage()) {
            clickedTile.swap(tiles[row][col - 1]);
        } else if (col < gridSize - 1 && tiles[row][col + 1].hasNoImage()) {
            clickedTile.swap(tiles[row][col + 1]);
        }

        if (imagesInOrder()) {
            tiles[gridSize - 1][gridSize - 1].showImage();
        }
    }

    private void scramble() {
        int openRow = gridSize - 1;
        int openCol = gridSize - 1;
        Random rand = new Random();
        for (int i = 0; i < 25 * gridSize; i++) {
            int direction = rand.nextInt(4);
            switch (direction) {
                case UP:
                    if (openRow > 0) {
                        tiles[openRow][openCol].swap(tiles[openRow - 1][openCol]);
                        openRow--;
                    }
                    break;
                case DOWN:
                    if (openRow < gridSize - 1) {
                        tiles[openRow][openCol].swap(tiles[openRow + 1][openCol]);
                        openRow++;
                    }
                    break;
                case LEFT:
                    if (openCol > 0) {
                        tiles[openRow][openCol].swap(tiles[openRow][openCol - 1]);
                        openCol--;
                    }
                    break;
                case RIGHT:
                    if (openCol < gridSize - 1) {
                        tiles[openRow][openCol].swap(tiles[openRow][openCol + 1]);
                        openCol++;
                    }
                    break;
            }
        }
    }

    private boolean imagesInOrder() {
        int id = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (tiles[row][col].getImageId() != id) {
                    return false;
                }
                id++;
            }
        }
        return true;
    }

    private void newGame() {
        int imageId = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int x = col * tileSize;
                int y = row * tileSize;
                BufferedImage subImage = image.getSubimage(x, y, tileSize, tileSize);
                ImageIcon imageIcon = new ImageIcon(subImage);
                tiles[row][col].setImage(imageIcon, imageId);
                imageId++;
            }
        }
        scramble();
    }

    public static void main(String[] args) {
        try {
//            String className = UIManager.getCrossPlatformLookAndFeelClassName();
            String className = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SlidingTiles();
            }
        });
    }
}
