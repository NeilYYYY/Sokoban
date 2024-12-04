package view.game;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ClosedDoor extends JComponent {
    private final Image image;

    public ClosedDoor(int width, int height) {
        this.setSize(width, height);
        this.setLocation(5, 5);
        this.image = new ImageIcon("src/images/ClosedDoor.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
    }

    @Override
    public void paintComponent(@NotNull Graphics g) {
        g.drawImage(image, 0, 0, this);
    }
}

