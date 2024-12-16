package view.game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Random;

public class GridComponent extends JComponent {
    private final int id; // represents the units digit value. It cannot be changed during one game.
    private final Image imageWall;
    private final Image imageFloor;
    private final Image imageButton;
    private Hero hero;
    private Box box;
    private OpenDoor openDoor;
    private ClosedDoor closedDoor;
    private Fragile fragile;

    public GridComponent(int id, int gridSize) {
        switch (new Random().nextInt(6) + 1) {
            case 1 ->
                    this.imageWall = new ImageIcon("resources/images/Wall1.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            case 2 ->
                    this.imageWall = new ImageIcon("resources/images/Wall2.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            case 3 ->
                    this.imageWall = new ImageIcon("resources/images/Wall3.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            case 4 ->
                    this.imageWall = new ImageIcon("resources/images/Wall4.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            case 5 ->
                    this.imageWall = new ImageIcon("resources/images/Wall5.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            default ->
                    this.imageWall = new ImageIcon("resources/images/Wall6.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        }
        this.imageFloor = new ImageIcon("resources/images/Floor.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        this.imageButton = new ImageIcon("resources/images/Button.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        this.setSize(gridSize, gridSize);
        this.id = id;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.printComponents(g);
        Color borderColor;
        switch (id % 10) {
            case 1 -> {
                g.drawImage(imageWall, 0, 0, this);
                borderColor = Color.DARK_GRAY;
            }
            case 2 -> {
                g.drawImage(imageFloor, 0, 0, this);
                borderColor = Color.DARK_GRAY;
                g.setColor(Color.GREEN);
                int[] xPoints = {getWidth() / 2, getWidth() * 3 / 4, getWidth() / 2, getWidth() / 4};
                int[] yPoints = {getHeight() / 4, getHeight() / 2, getHeight() * 3 / 4, getHeight() / 2};
                g.fillPolygon(xPoints, yPoints, 4);
                g.setColor(Color.BLACK);
                g.drawPolygon(xPoints, yPoints, 4);
            }
            default -> {
                g.drawImage(imageFloor, 0, 0, this);
                if (id == 100) {
                    g.drawImage(imageButton, 5, 5, this);
                }
                borderColor = Color.DARK_GRAY;
            }
        }
        Border border = BorderFactory.createLineBorder(borderColor, 0);//不知道为什么这段代码删了会有BUG，就把厚度设置成0。
        this.setBorder(border);
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

    public void setOpenDoorInGrid(OpenDoor openDoor) {
        this.openDoor = openDoor;
        this.add(openDoor);
    }

    public void setClosedDoorInGrid(ClosedDoor closedDoor) {
        this.closedDoor = closedDoor;
        this.add(closedDoor);
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
    public void removeBoxFromGrid() {
        this.remove(this.box);//remove box component from grid component
        this.box = null;//set the hero attribute to null
        this.revalidate();//Update component painting in real time
        this.repaint();
    }

    public void removeOpenDoorFromGrid() {
        this.remove(this.openDoor);
        this.openDoor = null;
        this.revalidate();
        this.repaint();
    }

    public void removeClosedDoorFromGrid() {
        this.remove(this.closedDoor);
        this.closedDoor = null;
        this.revalidate();
        this.repaint();
    }

    public void setFragileInGrid(Fragile fragile) {
        this.fragile = fragile;
        this.add(fragile);
    }

    public void removeFragileFromGrid() {
        this.remove(this.fragile);
        this.fragile = null;
        this.revalidate();
        this.repaint();
    }
}
