package view.game;

import org.jetbrains.annotations.NotNull;
import view.login.User;

import javax.swing.*;
import java.awt.*;

public class Box extends JComponent {
    private final Image image;

    public Box(int width, int height, @NotNull User user) {
        this.setSize(width, height);
        this.setLocation(5, 5);
        if (user.id() == 2) {
            this.image = new ImageIcon("src/images/The_Knight.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        } else {
            this.image = new ImageIcon("src/images/Grub.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        }
    }

    @Override
    public void paintComponent(@NotNull Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}
