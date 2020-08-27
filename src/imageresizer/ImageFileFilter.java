package imageresizer;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ImageFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        String fileName = f.getName().toLowerCase();
        return fileName.endsWith(".jpg")
                || fileName.endsWith(".png")
                || fileName.endsWith(".gif")
                || f.isDirectory();
    }

    @Override
    public String getDescription() {
        return "All image files: *.jpg, *.png, *.gif";
    }
}
