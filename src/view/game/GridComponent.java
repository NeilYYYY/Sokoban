package view.game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Random;

public class GridComponent extends JComponent {
    static Color color = new Color(246, 246, 229);
    private final int id; // represents the units digit value. It cannot be changed during one game.
    private final Image imageWall;
    private final Image imageFloor;
    private int row;
    private int col;
    private Hero hero;
    private Box box;

    public GridComponent(int row, int col, int id, int gridSize) {
        Random random = new Random();
        int randomNum = random.nextInt(3) + 1;
        switch (randomNum) {
            case 1 ->{
                this.imageWall = new ImageIcon("src/images/Wall1.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            }
            case 2 ->{
                this.imageWall = new ImageIcon("src/images/Wall2.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            }
            default ->{
                this.imageWall = new ImageIcon("src/images/Wall3.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            }
        }
        this.imageFloor = new ImageIcon("src/images/Floor.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        this.setSize(gridSize, gridSize);
        this.row = row;
        this.col = col;
        this.id = id;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.printComponents(g);
        Color borderColor = color;
        switch (id % 10) {
            case 1:
                g.drawImage(imageWall, 0, 0, this);
                borderColor = Color.DARK_GRAY;
                break;
            case 0:
                g.drawImage(imageFloor, 0, 0, this);
                borderColor = Color.DARK_GRAY;
                break;
            case 2:
                g.drawImage(imageFloor, 0, 0, this);
                borderColor = Color.DARK_GRAY;
                g.setColor(Color.GREEN);
                int[] xPoints = {getWidth() / 2, getWidth() * 3 / 4, getWidth() / 2, getWidth() / 4};
                int[] yPoints = {getHeight() / 4, getHeight() / 2, getHeight() * 3 / 4, getHeight() / 2};
                g.fillPolygon(xPoints, yPoints, 4);
                g.setColor(Color.BLACK);
                g.drawPolygon(xPoints, yPoints, 4);
                break;
        }
        Border border = BorderFactory.createLineBorder(borderColor, 0);//不知道为什么这段代码删了会有BUG，就把厚度设置成0。
        this.setBorder(border);
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

    public int getId() {
        return id;
    }

    //When adding a hero in this grid, invoking this method.
    public void setHeroInGrid(Hero hero) {
        this.hero = hero;
        this.add(hero);
    }

    //When adding a box in this grid, invoking this method.
    public void setBoxInGrid(Box box) {
        this.box = box;
        this.add(box);
    }

    //When removing hero from this grid, invoking this method
    public Hero removeHeroFromGrid() {
        this.remove(this.hero);//remove hero component from grid component
        Hero h = this.hero;
        this.hero = null;//set the hero attribute to null
        this.revalidate();//Update component painting in real time
        this.repaint();
        return h;
    }

    //When removing box from this grid, invoking this method
    public Box removeBoxFromGrid() {
        this.remove(this.box);//remove box component from grid component
        Box b = this.box;
        this.box = null;//set the hero attribute to null
        this.revalidate();//Update component painting in real time
        this.repaint();
        return b;
    }
}
