package view.level;

import java.awt.*;
import java.util.logging.Logger;
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
    private final Sound sound;
    private final int time;
    private int lv = 0;
    private boolean mode;
    private JLabel bg;

    public LevelFrame(User user, Sound sound, boolean mode) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            Logger log = Logger.getLogger(LevelFrame.class.getName());
            log.info(e.getMessage());
        }
        this.mode = mode;
        this.setTitle("Level");
        this.setLayout(null);
        this.setSize(800, 450);
        this.sound = sound;
        this.time = 60;
        this.setResizable(false);
        JButton level1Btn = FrameUtil.createButton(this, "Level 1", new Point(180, 155), 80, 60);
        Font f = new Font("Comic Sans MS", Font.BOLD, 16);
        level1Btn.setFont(f);
        level1Btn.setForeground(Color.WHITE);
        level1Btn.setMargin(new Insets(0, 0, 0, 0));
        level1Btn.setBorderPainted(false);
        level1Btn.setBorder(null);
        level1Btn.setFocusPainted(false);
        level1Btn.setContentAreaFilled(false);
        JButton level2Btn = FrameUtil.createButton(this, "Level 2", new Point(270, 155), 80, 60);
        level2Btn.setFont(f);
        level2Btn.setForeground(Color.WHITE);
        level2Btn.setMargin(new Insets(0, 0, 0, 0));
        level2Btn.setBorderPainted(false);
        level2Btn.setBorder(null);
        level2Btn.setFocusPainted(false);
        level2Btn.setContentAreaFilled(false);
        JButton level3Btn = FrameUtil.createButton(this, "Level 3", new Point(360, 155), 80, 60);
        level3Btn.setFont(f);
        level3Btn.setForeground(Color.WHITE);
        level3Btn.setMargin(new Insets(0, 0, 0, 0));
        level3Btn.setBorderPainted(false);
        level3Btn.setBorder(null);
        level3Btn.setFocusPainted(false);
        level3Btn.setContentAreaFilled(false);
        JButton level4Btn = FrameUtil.createButton(this, "Level 4", new Point(450, 155), 80, 60);
        level4Btn.setFont(f);
        level4Btn.setForeground(Color.WHITE);
        level4Btn.setMargin(new Insets(0, 0, 0, 0));
        level4Btn.setBorderPainted(false);
        level4Btn.setBorder(null);
        level4Btn.setFocusPainted(false);
        level4Btn.setContentAreaFilled(false);
        JButton level5Btn = FrameUtil.createButton(this, "Level 5", new Point(540, 155), 80, 60);
        level5Btn.setFont(f);
        level5Btn.setForeground(Color.WHITE);
        level5Btn.setMargin(new Insets(0, 0, 0, 0));
        level5Btn.setBorderPainted(false);
        level5Btn.setBorder(null);
        level5Btn.setFocusPainted(false);
        level5Btn.setContentAreaFilled(false);
        JButton logoutBtn = FrameUtil.createButton(this, "Logout", new Point(300, 275), 80, 60);
        logoutBtn.setFont(f);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setMargin(new Insets(0, 0, 0, 0));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setBorder(null);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setContentAreaFilled(false);
        JButton musicBtn = FrameUtil.createButton(this, "Music", new Point(400, 275), 80, 60);
        musicBtn.setFont(f);
        musicBtn.setForeground(Color.WHITE);
        musicBtn.setMargin(new Insets(0, 0, 0, 0));
        musicBtn.setBorderPainted(false);
        musicBtn.setBorder(null);
        musicBtn.setFocusPainted(false);
        musicBtn.setContentAreaFilled(false);
        JButton changeModeBtn = FrameUtil.createButton(this, "Change Mode", new Point(10, 350), 140, 60);
        changeModeBtn.setFont(f);
        changeModeBtn.setForeground(Color.WHITE);
        changeModeBtn.setMargin(new Insets(0, 0, 0, 0));
        changeModeBtn.setBorderPainted(false);
        changeModeBtn.setBorder(null);
        changeModeBtn.setFocusPainted(false);
        changeModeBtn.setContentAreaFilled(false);
        this.user = user;
        System.out.println(this.user);
        if (this.mode) {
            ImageIcon back = new ImageIcon("src/images/FileFrameBackground.png");
            back.setImage(back.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
            bg = new JLabel(back);
            bg.setBounds(0, 0, this.getWidth(), this.getHeight());
            this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层
        } else {
            ImageIcon back = new ImageIcon("src/images/Menu_Theme_Voidheart.png");
            back.setImage(back.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
            bg = new JLabel(back);
            bg.setBounds(0, 0, this.getWidth(), this.getHeight());
            this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层
        }


        level1Btn.addActionListener(_ -> {
            this.lv = 1;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_1.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound, this.mode, this.time);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level2Btn.addActionListener(_ -> {
            this.lv = 2;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_2.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound, this.mode, this.time);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level3Btn.addActionListener(_ -> {
            this.lv = 3;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_3.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound, this.mode, this.time);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level4Btn.addActionListener(_ -> {
            this.lv = 4;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_4.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound, this.mode, this.time);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level5Btn.addActionListener(_ -> {
            this.lv = 5;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_5.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound, this.mode, this.time);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });
        logoutBtn.addActionListener(_ -> {
            this.setVisible(false);
            LoginFrame loginFrame = new LoginFrame(this.sound);
            loginFrame.setVisible(true);
        });
        changeModeBtn.addActionListener(_ -> {
            //change bg
            this.mode = !this.mode;
            System.out.println("change mode");
            if (this.mode) {
                this.getContentPane().remove(bg);
                ImageIcon back = new ImageIcon("src/images/FileFrameBackground.png");
                back.setImage(back.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
                bg = new JLabel(back);
                bg.setBounds(0, 0, this.getWidth(), this.getHeight());
                this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层
                this.getContentPane().repaint();
            } else {
                this.getContentPane().remove(bg);
                ImageIcon back = new ImageIcon("src/images/Menu_Theme_Voidheart.png");
                back.setImage(back.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
                bg = new JLabel(back);
                bg.setBounds(0, 0, this.getWidth(), this.getHeight());
                this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层
                this.getContentPane().repaint();
            }
        });
        musicBtn.addActionListener(_ -> new MusicFrame(this, this.sound));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

