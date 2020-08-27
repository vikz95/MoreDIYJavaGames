package mycomponents;

import javax.swing.*;
import java.awt.*;

public class TitleLabel extends JLabel {

    public TitleLabel(String title) {
        Font titleFont = new Font(Font.SERIF, Font.BOLD, 32);
        setFont(titleFont);
        setText(title);
        setHorizontalAlignment(JLabel.CENTER);
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setOpaque(true);
    }
}
