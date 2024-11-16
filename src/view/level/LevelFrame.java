package view.level;

import java.awt.*;
import javax.swing.*;

import model.Level;
import model.MapMatrix;
import model.Sound;
import view.FrameUtil;
import view.game.GameFrame;
import view.login.LoginFrame;
import view.login.User;

public class LevelFrame extends JFrame {
    private final User user;
    private int lv = 0;
    private final Sound sound;

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
        this.sound = new Sound("src/misc/Alphys.wav");
        JButton level1Btn = FrameUtil.createButton(this, "Level 1", new Point(180, 160), 80, 60);
        JButton level2Btn = FrameUtil.createButton(this, "Level 2", new Point(270, 160), 80, 60);
        JButton level3Btn = FrameUtil.createButton(this, "Level 3", new Point(360, 160), 80, 60);
        JButton level4Btn = FrameUtil.createButton(this, "Level 4", new Point(450, 160), 80, 60);
        JButton level5Btn = FrameUtil.createButton(this, "Level 5", new Point(540, 160), 80, 60);
        JButton logoutBtn = FrameUtil.createButton(this, "Logout", new Point(360, 275), 80, 60);
        this.user = user;
        System.out.println(this.user);
        level1Btn.addActionListener(_ -> {
            this.lv = 1;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_1.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0);
            this.setVisible(false);
            gameFrame.setVisible(true);
            this.sound.stop();
        });

        level2Btn.addActionListener(_ -> {
            this.lv = 2;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_2.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0);
            this.setVisible(false);
            gameFrame.setVisible(true);
            this.sound.stop();
        });

        level3Btn.addActionListener(_ -> {
            this.lv = 3;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_3.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0);
            this.setVisible(false);
            gameFrame.setVisible(true);
            this.sound.stop();
        });

        level4Btn.addActionListener(_ -> {
            this.lv = 4;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_4.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0);
            this.setVisible(false);
            gameFrame.setVisible(true);
            this.sound.stop();
        });

        level5Btn.addActionListener(_ -> {
            this.lv = 5;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_5.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0);
            this.setVisible(false);
            gameFrame.setVisible(true);
            this.sound.stop();
        });
        logoutBtn.addActionListener(_ -> {
            this.setVisible(false);
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.sound.stop();
        });
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public Sound getSound() {
        return sound;
    }
}

