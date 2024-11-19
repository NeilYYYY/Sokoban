package view.level;

import java.awt.*;
import javax.swing.*;

import model.Level;
import model.MapMatrix;
import view.FrameUtil;
import view.game.GameFrame;
import view.login.LoginFrame;
import view.login.User;
import view.music.MusicFrame;
import view.music.Sound;

public class LevelFrame extends JFrame {
    private final User user;
    private int lv = 0;
    private final Sound sound;
    private final Font f = new Font("Comic Sans MS", Font.PLAIN, 13);

    public LevelFrame(User user, Sound sound) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTitle("Level");
        this.setLayout(null);
        this.setSize(800, 450);
        this.sound = sound;
        this.setResizable(false);
        JButton level1Btn = FrameUtil.createButton(this, "Level 1", new Point(180, 155), 80, 60);
        level1Btn.setFont(f);
        JButton level2Btn = FrameUtil.createButton(this, "Level 2", new Point(270, 155), 80, 60);
        level2Btn.setFont(f);
        JButton level3Btn = FrameUtil.createButton(this, "Level 3", new Point(360, 155), 80, 60);
        level3Btn.setFont(f);
        JButton level4Btn = FrameUtil.createButton(this, "Level 4", new Point(450, 155), 80, 60);
        level4Btn.setFont(f);
        JButton level5Btn = FrameUtil.createButton(this, "Level 5", new Point(540, 155), 80, 60);
        level5Btn.setFont(f);
        JButton logoutBtn = FrameUtil.createButton(this, "Logout", new Point(300, 275), 80, 60);
        logoutBtn.setFont(f);
        JButton musicBtn = FrameUtil.createButton(this, "Music", new Point(400, 275), 80, 60);
        musicBtn.setFont(f);
        this.user = user;
        System.out.println(this.user);
        level1Btn.addActionListener(_ -> {
            this.lv = 1;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_1.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level2Btn.addActionListener(_ -> {
            this.lv = 2;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_2.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level3Btn.addActionListener(_ -> {
            this.lv = 3;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_3.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level4Btn.addActionListener(_ -> {
            this.lv = 4;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_4.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level5Btn.addActionListener(_ -> {
            this.lv = 5;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_5.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });
        logoutBtn.addActionListener(_ -> {
            this.setVisible(false);
            LoginFrame loginFrame = new LoginFrame(this.sound);
            loginFrame.setVisible(true);
        });
        musicBtn.addActionListener(_ -> new MusicFrame(this, this.sound));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

