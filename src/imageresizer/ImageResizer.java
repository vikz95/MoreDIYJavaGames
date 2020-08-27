package imageresizer;

import mycomponents.TitleLabel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResizer extends JFrame {

    private static final String DIR = "src/imageresizer/resources";
    private static final String IMAGE_LOAD = "/Load16.gif";
    private static final String IMAGE_SAVE = "/Save16.gif";
    private static final String IMAGE_SCALE = "/Scale16.gif";
    private static final String IMAGE_WIDTH = "/Width16.gif";
    private static final String IMAGE_HEIGHT = "/Height16.gif";
    private static final String IMAGE_CROP = "/Crop16.gif";
    private static final String IMAGE_X = "/x16.gif";
    private static final String IMAGE_Y = "/y16.gif";

    private ImagePanel imagePanel = new ImagePanel(this);
    private File file = new File("");
    private double ratio = 1.0;

    private JTextField scaleWField = new JTextField("1", 5);
    private JTextField scaleHField = new JTextField("1", 5);
    private JTextField cropXField = new JTextField("0", 5);
    private JTextField cropYField = new JTextField("0", 5);
    private JTextField cropWField = new JTextField("0", 5);
    private JTextField cropHField = new JTextField("0", 5);

    public ImageResizer() {
        initGUI();

        setTitle("Image Resizer");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initGUI() {
        TitleLabel titleLabel = new TitleLabel("Image Resizer");
        add(titleLabel, BorderLayout.PAGE_START);

        // main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // toolbar
        ImageIcon loadIcon = new ImageIcon(DIR + IMAGE_LOAD);
        ImageIcon saveIcon = new ImageIcon(DIR + IMAGE_SAVE);
        ImageIcon scaleIcon = new ImageIcon(DIR + IMAGE_SCALE);
        ImageIcon widthIcon = new ImageIcon(DIR + IMAGE_WIDTH);
        ImageIcon heightIcon = new ImageIcon(DIR + IMAGE_HEIGHT);
        ImageIcon cropIcon = new ImageIcon(DIR + IMAGE_CROP);
        ImageIcon xIcon = new ImageIcon(DIR + IMAGE_X);
        ImageIcon yIcon = new ImageIcon(DIR + IMAGE_Y);

        JToolBar toolbar = new JToolBar();
        mainPanel.add(toolbar, BorderLayout.PAGE_START);

        JButton loadButton = new JButton(loadIcon);
        loadButton.setToolTipText("Load Image");
        loadButton.addActionListener(e -> load());
        toolbar.add(loadButton);

        JButton saveButton = new JButton(saveIcon);
        saveButton.setToolTipText("Save Image");
        saveButton.addActionListener(e -> save());
        toolbar.add(saveButton);

        toolbar.addSeparator();

        // scale options
        JButton scaleButton = new JButton(scaleIcon);
        scaleButton.setToolTipText("Scale Image");
        scaleButton.addActionListener(e -> scale());
        toolbar.add(scaleButton);

        JLabel scaleWLabel = new JLabel(widthIcon);
        toolbar.add(scaleWLabel);
        scaleWField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                changedScaleW();
            }
        });
        toolbar.add(scaleWField);

        JLabel scaleHLabel = new JLabel(heightIcon);
        toolbar.add(scaleHLabel);
        scaleHField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                changedScaleH();
            }
        });
        toolbar.add(scaleHField);

        toolbar.addSeparator();

        // crop options
        JButton cropButton = new JButton(cropIcon);
        cropButton.setToolTipText("Crop Image");
        cropButton.addActionListener(e -> crop());
        toolbar.add(cropButton);

        JLabel cropXLabel = new JLabel(xIcon);
        toolbar.add(cropXLabel);
        cropXField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                changedCropX();
            }
        });
        toolbar.add(cropXField);

        JLabel cropYLabel = new JLabel(yIcon);
        toolbar.add(cropYLabel);
        cropYField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                changedCropY();
            }
        });
        toolbar.add(cropYField);

        JLabel cropWLabel = new JLabel(widthIcon);
        toolbar.add(cropWLabel);
        cropWField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                changedCropW();
            }
        });
        toolbar.add(cropWField);

        JLabel cropHLabel = new JLabel(heightIcon);
        toolbar.add(cropHLabel);
        cropHField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                changedCropH();
            }
        });
        toolbar.add(cropHField);

        // image panel
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void load() {
        JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home"), "Pictures"));
        fileChooser.setFileFilter(new ImageFileFilter());
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(file);
                imagePanel.setImage(image);
                setScaleFields(image.getWidth(), image.getHeight());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Could not open file " + file.getPath());
            }
        }
    }

    private void save() {
        JFileChooser fileChooser = new JFileChooser(file);
        fileChooser.setSelectedFile(file);
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String oldFileName = file.getPath();
            file = fileChooser.getSelectedFile();
            String fileName = file.getPath();
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("gif")) {
                extension = oldFileName.substring(oldFileName.lastIndexOf('.') + 1);
                file = new File(fileName + "." + extension);
            }
            if (file.exists()) {
                String message = "Are you sure you want to replace " + file.getPath() + "?";
                int replace = JOptionPane.showConfirmDialog(this, message, "Replace Image?", JOptionPane.YES_NO_OPTION);
                if (replace == JOptionPane.YES_OPTION) {
                    writeImage(extension);
                }
            } else {
                writeImage(extension);
            }
        }
    }

    private void writeImage(String extension) {
        BufferedImage image = imagePanel.getImage();
        try {
            ImageIO.write(image, extension, file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not save " + file.getPath());
        }
    }

    private void setScaleFields(int width, int height) {
        scaleWField.setText("" + width);
        scaleHField.setText("" + height);
        ratio = (double) width / (double) height;
    }

    private void scale() {
        try {
            int width = Integer.parseInt(scaleWField.getText());
            int height = Integer.parseInt(scaleHField.getText());
            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = newImage.getGraphics();
            BufferedImage oldImage = imagePanel.getImage();
            g.drawImage(oldImage, 0, 0, width, height, this);
            g.dispose();
            imagePanel.setImage(newImage);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "The image could not be scaled.");
        }
    }

    private void changedScaleW() {
        String s = scaleWField.getText();
        if (isValid(s, 1)) {
            int scaleW = Integer.parseInt(scaleWField.getText());
            double dHeight = scaleW / ratio;
            int scaleH = (int) dHeight;
            if (scaleH < 1) {
                scaleH = 1;
            }
            scaleHField.setText("" + scaleH);
        } else {
            JOptionPane.showMessageDialog(this, s + " is not a valid width.");
            scaleWField.requestFocus();
        }
    }

    private void changedScaleH() {
        String s = scaleHField.getText();
        if (isValid(s, 1)) {
            int scaleH = Integer.parseInt(s);
            double dWidth = scaleH * ratio;
            int scaleW = (int) dWidth;
            if (scaleW < 1) {
                scaleW = 1;
            }
            scaleWField.setText("" + scaleW);
        } else {
            JOptionPane.showMessageDialog(this, s + " is not a valid height.");
            scaleHField.requestFocus();
        }
    }

    private void crop() {
        try {
            int cropX = Integer.parseInt(cropXField.getText());
            int cropY = Integer.parseInt(cropYField.getText());
            int cropW = Integer.parseInt(cropWField.getText());
            int cropH = Integer.parseInt(cropHField.getText());

            BufferedImage image = imagePanel.getImage();
            BufferedImage croppedImage = image.getSubimage(cropX, cropY, cropW, cropH);
            imagePanel.setImage(croppedImage);

            setScaleFields(cropW, cropH);
            setCropFields(0, 0, 0, 0);
            imagePanel.resetCrop();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image could not be cropped.");
        }
    }

    public void setCropFields(int cropX, int cropY, int cropW, int cropH) {
        cropXField.setText("" + cropX);
        cropYField.setText("" + cropY);
        cropWField.setText("" + cropW);
        cropHField.setText("" + cropH);
    }

    private void changedCropX() {
        String s = cropXField.getText();
        if (isValid(s, 0)) {
            int cropX = Integer.parseInt(s);
            imagePanel.setCropX(cropX);
        } else {
            JOptionPane.showMessageDialog(this, s + " is not a valid crop x value.");
            cropXField.requestFocus();
        }
    }

    private void changedCropY() {
        String s = cropYField.getText();
        if (isValid(s, 0)) {
            int cropY = Integer.parseInt(s);
            imagePanel.setCropY(cropY);
        } else {
            JOptionPane.showMessageDialog(this, s + " is not a valid crop x value.");
            cropYField.requestFocus();
        }
    }

    private void changedCropW() {
        String s = cropWField.getText();
        if (isValid(s, 0)) {
            int cropW = Integer.parseInt(s);
            imagePanel.setCropW(cropW);
        } else {
            JOptionPane.showMessageDialog(this, s + " is not a valid crop x value.");
            cropWField.requestFocus();
        }
    }

    private void changedCropH() {
        String s = cropHField.getText();
        if (isValid(s, 0)) {
            int cropH = Integer.parseInt(s);
            imagePanel.setCropH(cropH);
        } else {
            JOptionPane.showMessageDialog(this, s + " is not a valid crop x value.");
            cropHField.requestFocus();
        }
    }

    private boolean isValid(String s, int minValue) {
        try {
            int i = Integer.parseInt(s);
            return i >= minValue;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            String className = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(ImageResizer::new);
    }
}
