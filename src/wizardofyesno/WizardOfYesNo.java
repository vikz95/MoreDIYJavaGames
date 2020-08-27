package wizardofyesno;

import mycomponents.TitleLabel;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class WizardOfYesNo extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String[] ANSWER = {
            "Yes.",
            "Go for it!",
            "Yes, definitely",
            "For Sure!",
            "I would say yes.",
            "Most likely yes.",
            "No.",
            "I wouldn't.",
            "In my opinion, no.",
            "Definitely not!",
            "Probably not",
            "It is very doubtful"
    };

    public WizardOfYesNo() {
        TitleLabel titleLabel = new TitleLabel("Wizard of Yes/No");
        add(titleLabel, BorderLayout.PAGE_START);

        Random rand = new Random();
        int numberOfAnswers = ANSWER.length;
        int pick = rand.nextInt(numberOfAnswers);
        String answer = ANSWER[pick];

        JLabel label = new JLabel();
        label.setText(answer);
        Font font = new Font(Font.SERIF, Font.BOLD, 32);
        label.setFont(font);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        if (pick <= 5) {
            label.setBackground(Color.GREEN);
        } else {
            label.setBackground(Color.RED);
        }
        add(label, BorderLayout.CENTER);

        String disclaimer = "This is only a suggestion. Use your own judgment.";
        JTextArea disclaimerTextArea = new JTextArea(disclaimer);
        disclaimerTextArea.setLineWrap(true); // if text doesn't fit it is displayed on new line
        disclaimerTextArea.setWrapStyleWord(true); // split lines at word boundaries, instead of characters
        disclaimerTextArea.setEditable(false);
//        add(disclaimerTextArea, BorderLayout.PAGE_END);
        JScrollPane scrollPane = new JScrollPane(disclaimerTextArea);
        scrollPane.setPreferredSize(new Dimension(0, 25)); // 0 means wide as the window
        add(scrollPane, BorderLayout.PAGE_END);

        setTitle("Wizard of Yes/No");
//        setResizable(false);
//        setSize(800, 700);
        pack(); // windows size chosen to fit content
        setLocationRelativeTo(null); // the window appears in the center of the screen
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        try {
            // same UI on every OS
            String className = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WizardOfYesNo();
            }
        });
    }
}
