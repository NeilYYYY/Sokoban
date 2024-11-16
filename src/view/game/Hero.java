package view.game;

import javax.swing.*;
import java.awt.*;

public class Hero extends JComponent {
    private final int value = 20;
    private int row;
    private int col;
    private final Image image;

    public Hero(int width, int height, int row, int col) {
        this.row = row;
        this.col = col;
        this.setSize(width, height);
        this.setLocation(8, 8);
        this.image = new ImageIcon("src/images/hero.png").getImage().getScaledInstance(34, 34, Image.SCALE_DEFAULT);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public int getValue() {
        return value;
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
