package view.game;

import javax.swing.*;
import java.awt.*;

public class Button extends JComponent {
    private final Image image;

    public Button(int width, int height) {
        this.image = new ImageIcon("src/images/button.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        this.setSize(width, height);
        this.setLocation(5, 5);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}
