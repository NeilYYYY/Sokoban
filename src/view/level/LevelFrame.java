package view.level;

import java.awt.*;
import javax.swing.*;

import model.MapMatrix;
import view.FrameUtil;
import view.game.GameFrame;
import view.login.LoginFrame;
import view.login.User;

public class LevelFrame extends JFrame {
    private final User user;
    private int lv = 0;

    public LevelFrame(User user) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTitle("Level");
        this.setLayout(null);
        this.setSize(800, 450);
        JButton level1Btn = FrameUtil.createButton(this, "Level 1", new Point(180, 175), 80, 60);
        JButton level2Btn = FrameUtil.createButton(this, "Level 2", new Point(270, 175), 80, 60);
        JButton level3Btn = FrameUtil.createButton(this, "Level 3", new Point(360, 175), 80, 60);
        JButton level4Btn = FrameUtil.createButton(this, "Level 4", new Point(450, 175), 80, 60);
        JButton level5Btn = FrameUtil.createButton(this, "Level 5", new Point(540, 175), 80, 60);
        JButton logoutBtn = FrameUtil.createButton(this, "Logout", new Point(360, 275), 80, 60);
        this.user = user;
        System.out.println(user);
        level1Btn.addActionListener(_ -> {
            this.lv = 1;
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {1, 1, 1, 1, 1, 1},
                    {1, 20, 0, 0, 0, 1},
                    {1, 0, 0, 10, 2, 1},
                    {1, 0, 2, 10, 0, 1},
                    {1, 1, 1, 1, 1, 1},
            });
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, user, lv);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level2Btn.addActionListener(_ -> {
            this.lv = 2;
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {1, 1, 1, 1, 1, 1, 0},
                    {1, 20, 0, 0, 0, 1, 1},
                    {1, 0, 10, 10, 0, 0, 1},
                    {1, 0, 1, 2, 0, 2, 1},
                    {1, 0, 0, 0, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1, 1},
            });
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, user, lv);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level3Btn.addActionListener(_ -> {
            this.lv = 3;
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {0, 0, 1, 1, 1, 1, 0},
                    {1, 1, 1, 0, 0, 1, 0},
                    {1, 20, 0, 2, 10, 1, 1},
                    {1, 0, 0, 0, 10, 0, 1},
                    {1, 0, 1, 2, 0, 0, 1},
                    {1, 0, 0, 0, 0, 0, 1},
                    {1, 1, 1, 1, 1, 1, 1},
            });
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, user, lv);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level4Btn.addActionListener(_ -> {
            this.lv = 4;
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {0, 1, 1, 1, 1, 1, 0},
                    {1, 1, 20, 0, 0, 1, 1},
                    {1, 0, 0, 1, 0, 0, 1},
                    {1, 0, 10, 12, 10, 0, 1},
                    {1, 0, 0, 2, 0, 0, 1},
                    {1, 1, 0, 2, 0, 1, 1},
                    {0, 1, 1, 1, 1, 1, 0},
            });
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, user, lv);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level5Btn.addActionListener(_ -> {
            this.lv = 5;
            MapMatrix mapMatrix = new MapMatrix(new int[][]{
                    {1, 1, 1, 1, 1, 1, 0, 0},
                    {1, 0, 0, 0, 0, 1, 1, 1},
                    {1, 0, 0, 0, 2, 2, 0, 1},
                    {1, 0, 10, 10, 10, 20, 0, 1},
                    {1, 0, 0, 1, 0, 2, 0, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1},
            });
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, user, lv);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });
        logoutBtn.addActionListener(_ -> {
            this.setVisible(false);
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

