package bakersdozen;

import mycomponents.TitleLabel;

import javax.swing.*;
import java.awt.*;

public class BakersDozen extends JFrame {

    private TablePanel tablePanel = new TablePanel();

    public BakersDozen() {
        initGUI();

        setTitle("Baker's Dozen");
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initGUI() {
        TitleLabel titleLabel = new TitleLabel("Baker's Dozen");
        add(titleLabel, BorderLayout.PAGE_START);

        // table panel
        add(tablePanel, BorderLayout.CENTER);

        // button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        add(buttonPanel, BorderLayout.PAGE_END);

        JButton newButton = new JButton("New Game");
        newButton.addActionListener(e -> tablePanel.newGame());
        buttonPanel.add(newButton);

        JButton replayButton = new JButton("Replay");
        replayButton.addActionListener(e -> tablePanel.replay());
        buttonPanel.add(replayButton);
    }

    public static void main(String[] args) {
        try {
            String className = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(BakersDozen::new);
    }
}
