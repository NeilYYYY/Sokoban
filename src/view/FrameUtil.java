package view;

import javax.swing.*;
import java.awt.*;

/**
 * This class is to create basic JComponent.
 */
public class FrameUtil {
    public static JLabel createJLabel(JFrame frame, String name, Font font, Point location, int width, int height) {
        JLabel label = new JLabel(name);
        label.setFont(font);
        label.setLocation(location);
        label.setSize(width, height);
        frame.getContentPane().add(label, Integer.valueOf(1));
        return label;
    }

    public static JButton createButton(JFrame frame, String name, Point location, int width, int height) {
        JButton button = new JButton(name);
        button.setLocation(location);
        button.setSize(width, height);
        frame.getContentPane().add(button, Integer.valueOf(1));
        return button;
    }
}
