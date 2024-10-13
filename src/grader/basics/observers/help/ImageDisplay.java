package grader.basics.observers.help;
import javax.swing.*;
import java.awt.*;

public class ImageDisplay extends JFrame {

    public ImageDisplay(Image image) {
        setTitle("Image Display");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);

        add(label);
        pack();
        setVisible(true);
    }
}
