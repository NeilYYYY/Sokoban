package view.level;

import java.awt.*;
import javax.swing.*;

import model.MapMatrix;
import view.FrameUtil;
import view.game.GameFrame;
import view.login.User;

public class LevelFrame extends JFrame {
    private final User user;

    public LevelFrame(int width, int height, User user) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTitle("Level");
        this.setLayout(null);
        this.setSize(width, height);
        JButton level1Btn = FrameUtil.createButton(this, "Level 1", new Point(30, height / 2 - 50), 80, 60);
        JButton level2Btn = FrameUtil.createButton(this, "Level 2", new Point(120, height / 2 - 50), 80, 60);
        JButton level3Btn = FrameUtil.createButton(this, "Level 3", new Point(210, height / 2 - 50), 80, 60);
        JButton level4Btn = FrameUtil.createButton(this, "Level 4", new Point(300, height / 2 - 50), 80, 60);
        JButton level5Btn = FrameUtil.createButton(this, "Level 5", new Point(390, height / 2 - 50), 80, 60);
        this.user = user;
        System.out.println(user);
        level1Btn.addActionListener(_ -> {
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {1, 1, 1, 1, 1, 1},
                    {1, 20, 0, 0, 0, 1},
                    {1, 0, 0, 10, 2, 1},
                    {1, 0, 2, 10, 0, 1},
                    {1, 1, 1, 1, 1, 1},
            });
            GameFrame gameFrame = new GameFrame(600, 450, mapMatrix, user);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level2Btn.addActionListener(_ -> {
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {1, 1, 1, 1, 1, 1, 0},
                    {1, 20, 0, 0, 0, 1, 1},
                    {1, 0, 10, 10, 0, 1, 1},
                    {1, 0, 1, 2, 0, 2, 1},
                    {1, 0, 0, 0, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1, 1},
            });
            GameFrame gameFrame = new GameFrame(600, 450, mapMatrix, user);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level3Btn.addActionListener(_ -> {
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {0, 0, 1, 1, 1, 1, 0},
                    {1, 1, 1, 0, 0, 1, 0},
                    {1, 20, 0, 2, 10, 1, 1},
                    {1, 0, 0, 0, 10, 0, 1},
                    {1, 0, 1, 2, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1, 1},
            });
            GameFrame gameFrame = new GameFrame(600, 450, mapMatrix, user);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level4Btn.addActionListener(_ -> {
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {0, 1, 1, 1, 1, 1, 0},
                    {1, 1, 20, 0, 0, 1, 1},
                    {1, 0, 0, 1, 0, 0, 1},
                    {1, 0, 10, 12, 10, 0, 1},
                    {1, 0, 1, 2, 0, 0, 1},
                    {1, 1, 0, 2, 0, 1, 1},
                    {0, 1, 1, 1, 1, 1, 0},
            });
            GameFrame gameFrame = new GameFrame(600, 450, mapMatrix, user);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level5Btn.addActionListener(_ -> {
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {1, 1, 1, 1, 1, 1, 0, 0},
                    {1, 0, 0, 0, 0, 1, 1, 1},
                    {1, 0, 0, 0, 2, 2, 0, 1},
                    {1, 0, 10, 10, 10, 20, 0, 1},
                    {1, 0, 0, 1, 0, 2, 0, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1},
            });
            GameFrame gameFrame = new GameFrame(600, 450, mapMatrix, user);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

