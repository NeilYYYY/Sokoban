package view.game;

import org.jetbrains.annotations.NotNull;
import view.login.User;

import javax.swing.*;
import java.awt.*;

public class Box extends JComponent {
    private final Image image;

    public Box(int width, int height, @NotNull User user, int value) {
        this.setSize(width, height);
        this.setLocation(5, 5);
        if (user.getId() == 2) {
            this.image = new ImageIcon("resources/images/The_Knight.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        } else {
            if (value == 12) {
                this.image = new ImageIcon("resources/images/FreeGrub.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            } else {
                this.image = new ImageIcon("resources/images/Grub.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            }
        }
    }

    @Override
    public void paintComponent(@NotNull Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}
