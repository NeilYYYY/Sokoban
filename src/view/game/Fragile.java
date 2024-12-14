package view.game;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class Fragile extends JComponent {
    private final Image image;

    public Fragile(int width, int height) {
        this.image = new ImageIcon("resources/images/404.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        this.setSize(width, height);
        this.setLocation(5, 5);
    }

    @Override
    public void paintComponent(@NotNull Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}

