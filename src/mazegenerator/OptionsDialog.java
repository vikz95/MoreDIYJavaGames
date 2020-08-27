package mazegenerator;

import javax.swing.*;
import java.awt.*;

public class OptionsDialog extends JDialog {

    private int rows = 0;
    private int cols = 0;
    private int type = 0;
    private boolean canceled = true;

    private JTextField rowsField = new JTextField(3);
    private JTextField colsField = new JTextField(3);
    private JRadioButton mazeButton = new JRadioButton("Maze");
    private JRadioButton antiMazeButton = new JRadioButton("Anti-Maze");

    public OptionsDialog(int rows, int cols, int type) {
        this.rows = rows;
        this.cols = cols;
        this.type = type;

        setTitle("Maze Generator Options");

        // main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel, BorderLayout.CENTER);

        JLabel rowsLabel = new JLabel("Rows:");
        mainPanel.add(rowsLabel);
        rowsField.setText("" + rows);
        mainPanel.add(rowsField);

        JLabel colsLabel = new JLabel("Columns:");
        mainPanel.add(colsLabel);
        colsField.setText("" + cols);
        mainPanel.add(colsField);

        JLabel typeLabel = new JLabel("Maze Type:");
        mainPanel.add(typeLabel);
        mainPanel.add(typeLabel);
        mainPanel.add(mazeButton);
        mainPanel.add(antiMazeButton);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(mazeButton);
        buttonGroup.add(antiMazeButton);
        if (type == MazeGenerator.TYPE_MAZE) {
            mazeButton.setSelected(true);
        } else {
            antiMazeButton.setSelected(true);
        }

        // button panel
        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.PAGE_END);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> close());
        buttonPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancel());
        buttonPanel.add(cancelButton);

        getRootPane().setDefaultButton(okButton);
    }

    private void close() {
        try {
            int newRows = Integer.parseInt(rowsField.getText());
            int newCols = Integer.parseInt(colsField.getText());
            if (newRows > 1 && newCols > 1) {
                rows = newRows;
                cols = newCols;
                if (mazeButton.isSelected()) {
                    type = MazeGenerator.TYPE_MAZE;
                } else {
                    type = MazeGenerator.TYPE_ANTIMAZE;
                }
                setVisible(false);
                canceled = false;
            } else {
                JOptionPane.showMessageDialog(this,
                        "There must be more than one row and more than one column.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Rows and columns must be numbers.");
        }
    }

    private void cancel() {
        setVisible(false);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMazeType() {
        return type;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
