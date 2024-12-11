package view.game;

import org.jetbrains.annotations.NotNull;
import view.login.User;

import javax.swing.*;
import java.awt.*;

public class Hero extends JComponent {
    private final Image image;
    private int row;
    private int col;

    public Hero(int width, int height, int row, int col, @NotNull User user) {
        this.row = row;
        this.col = col;
        this.setSize(width, height);
        this.setLocation(8, 8);
        if (user.getId() == 2) {
            this.image = new ImageIcon("resources/images/Grub.png").getImage().getScaledInstance(34, 34, Image.SCALE_SMOOTH);
        } else {
            this.image = new ImageIcon("resources/images/The_Knight.png").getImage().getScaledInstance(34, 34, Image.SCALE_DEFAULT);
        }
    }

    @Override
    public void paintComponent(@NotNull Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
