package framed;

import mycomponents.TitleLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Framed extends JFrame {

    private static final int GRIDSIZE = 3;
    private LightButton[][] lightButtons = new LightButton[GRIDSIZE][GRIDSIZE];

    public Framed() {
        initGUI();

        setTitle("Framed");
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        newGame();
    }

    private void initGUI() {
        TitleLabel titleLabel = new TitleLabel("Framed");
        add(titleLabel, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(GRIDSIZE, GRIDSIZE));
        add(centerPanel, BorderLayout.CENTER);
        for (int row = 0; row < GRIDSIZE; row++) {
            for (int col = 0; col < GRIDSIZE; col++) {
                LightButton lightButton = new LightButton(row, col);
                lightButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LightButton clickedButton = (LightButton) e.getSource();
                        toggleLights(clickedButton.getRow(), clickedButton.getCol());
                        endGameIfDone();
                    }
                });
                lightButtons[row][col] = lightButton;
                centerPanel.add(lightButtons[row][col]);
            }
        }
    }

    private void toggleLights(int row, int col) {
        lightButtons[row][col].toggle();

        // top left corner
        if (row == 0 && col == 0) {
            lightButtons[0][1].toggle();
            lightButtons[1][0].toggle();
            lightButtons[1][1].toggle();
        }
        // top right corner
        else if (row == 0 && col == 2) {
            lightButtons[0][1].toggle();
            lightButtons[1][1].toggle();
            lightButtons[1][2].toggle();
        }
        // bottom left corner
        else if (row == 2 && col == 0) {
            lightButtons[1][0].toggle();
            lightButtons[1][1].toggle();
            lightButtons[2][1].toggle();
        }
        // bottom right corner
        else if (row == 2 && col == 2) {
            lightButtons[1][2].toggle();
            lightButtons[1][1].toggle();
            lightButtons[2][1].toggle();
        }
        // top row middle
        else if (row == 0 && col == 1) {
            lightButtons[0][0].toggle();
            lightButtons[0][2].toggle();
        }
        // bottom row middle
        else if (row == 2 && col == 1) {
            lightButtons[2][0].toggle();
            lightButtons[2][2].toggle();
        }
        // left side middle
        else if (row == 1 && col == 0) {
            lightButtons[0][0].toggle();
            lightButtons[2][0].toggle();
        }
        // right side middle
        else if (row == 1 && col == 2) {
            lightButtons[0][2].toggle();
            lightButtons[2][2].toggle();
        }
        // center
        else if (row == 1 && col == 1) {
            lightButtons[0][1].toggle();
            lightButtons[2][1].toggle();
            lightButtons[1][0].toggle();
            lightButtons[1][2].toggle();
        }
    }

    private void newGame() {
        // turn all lights on, the turn middle light off, program starts with a completed frame
        for (int row = 0; row < GRIDSIZE; row++) {
            for (int col = 0; col < GRIDSIZE; col++) {
                lightButtons[row][col].turnOn();
            }
        }
        lightButtons[1][1].turnOff();

        // random game initialization, guaranteed to be winnable
        Random rand = new Random();
        int numberOfTimes = rand.nextInt(10) + 10;
        for (int i = 0; i < numberOfTimes; i++) {
            int row = rand.nextInt(GRIDSIZE);
            int col = rand.nextInt(GRIDSIZE);
            toggleLights(row, col);
        }
    }

    private void endGameIfDone() {
        boolean done = lightButtons[0][0].isLit()
                && lightButtons[0][1].isLit()
                && lightButtons[0][2].isLit()
                && lightButtons[1][0].isLit()
                && !lightButtons[1][1].isLit()
                && lightButtons[1][2].isLit()
                && lightButtons[2][0].isLit()
                && lightButtons[2][1].isLit()
                && lightButtons[2][2].isLit();

        if (done) {
            String message = "Congratulations! You won! Do you want to play again?";
            int option = JOptionPane.showConfirmDialog(this, message, "Play again?", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                newGame();
            } else {
                System.exit(0);
            }
        }
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
                new Framed();
            }
        });
    }
}
