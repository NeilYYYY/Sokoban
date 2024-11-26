package view.game;

import javax.swing.*;
import java.awt.*;

public class Fragile extends JComponent {
    private final Image image;

    public Fragile(int width, int height) {
        this.image = new ImageIcon("src/images/404.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        this.setSize(width, height);
        this.setLocation(5, 5);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}

