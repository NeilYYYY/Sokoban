package view.game;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class Box extends JComponent {
    private final Image image;

    public Box(int width, int height) {
        this.image = new ImageIcon("src/images/Grub.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        this.setSize(width, height);
        this.setLocation(5, 5);
    }

    public void paintComponent(@NotNull Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}
