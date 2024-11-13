package view.game;

import javax.swing.*;
import java.awt.*;

public class Hero extends JComponent {
    private static Color color = new Color(0, 255, 255, 128);
    private final int value = 20;
    private int row;
    private int col;

    public Hero(int width, int height, int row, int col) {
        this.row = row;
        this.col = col;
        this.setSize(width, height);
        this.setLocation(8, 8);
    }

    public void paintComponent(Graphics g) {
        g.setColor(color);
        g.fillOval(0, 0, getWidth(), getHeight());
        g.setColor(color);
        g.fillOval(1, 1, getWidth() - 2, getHeight() - 2);
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
