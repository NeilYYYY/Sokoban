package view.game;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class OpenDoor extends JComponent {
    private final Image image;

    public OpenDoor(int width, int height) {
        this.image = new ImageIcon("resources/images/OpenDoor.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        this.setSize(width, height);
        this.setLocation(5, 5);
    }

    @Override
    public void paintComponent(@NotNull Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}
