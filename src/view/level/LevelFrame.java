package view.level;

import java.awt.*;
import java.util.Random;
import java.util.logging.Logger;
import javax.swing.*;

import model.Level;
import model.MapMatrix;
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
    private LevelParticleEffect panel;

    public LevelFrame(User user, Sound sound, boolean mode, boolean flag) {
        try {
            String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            Logger log = Logger.getLogger(LevelFrame.class.getName());
            log.info(e.getMessage());
        }
        this.setTitle("Level");
        this.setLayout(null);
        this.setSize(800, 450);
        this.sound = sound;
        this.time = new Random(System.currentTimeMillis()).nextInt(121) + 30;
        this.setResizable(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.setContentPane(layeredPane);

        panel = new LevelParticleEffect(50, mode);
        panel.setBounds(0, 0, 800, 450);
        panel.setOpaque(false);
        this.getContentPane().add(panel, Integer.valueOf(0));

        Font f = new Font("Comic Sans MS", Font.BOLD, 16);
        this.mode = mode;
        this.user = user;

        JButton level1Btn = new JButton("Level 1");
        level1Btn.setLocation(new Point(170, 155));
        level1Btn.setSize(80, 60);
        level1Btn.setFont(f);
        level1Btn.setForeground(Color.WHITE);
        level1Btn.setMargin(new Insets(0, 0, 0, 0));
        level1Btn.setBorderPainted(false);
        level1Btn.setBorder(null);
        level1Btn.setFocusPainted(false);
        level1Btn.setContentAreaFilled(false);
        getContentPane().add(level1Btn, Integer.valueOf(1));
        if (flag) {
            level1Btn.setVisible(false);
        }

        JButton level2Btn = new JButton("Level 2");
        level2Btn.setLocation(new Point(260, 155));
        level2Btn.setSize(80, 60);
        level2Btn.setFont(f);
        level2Btn.setForeground(Color.WHITE);
        level2Btn.setMargin(new Insets(0, 0, 0, 0));
        level2Btn.setBorderPainted(false);
        level2Btn.setBorder(null);
        level2Btn.setFocusPainted(false);
        level2Btn.setContentAreaFilled(false);
        getContentPane().add(level2Btn, Integer.valueOf(1));
        if (flag) {
            level2Btn.setVisible(false);
        }

        JButton level3Btn = new JButton("Level 3");
        level3Btn.setLocation(new Point(350, 155));
        level3Btn.setSize(80, 60);
        level3Btn.setFont(f);
        level3Btn.setForeground(Color.WHITE);
        level3Btn.setMargin(new Insets(0, 0, 0, 0));
        level3Btn.setBorderPainted(false);
        level3Btn.setBorder(null);
        level3Btn.setFocusPainted(false);
        level3Btn.setContentAreaFilled(false);
        getContentPane().add(level3Btn, Integer.valueOf(1));
        if (flag) {
            level3Btn.setVisible(false);
        }

        JButton level4Btn = new JButton("Level 4");
        level4Btn.setLocation(new Point(440, 155));
        level4Btn.setSize(80, 60);
        level4Btn.setFont(f);
        level4Btn.setForeground(Color.WHITE);
        level4Btn.setMargin(new Insets(0, 0, 0, 0));
        level4Btn.setBorderPainted(false);
        level4Btn.setBorder(null);
        level4Btn.setFocusPainted(false);
        level4Btn.setContentAreaFilled(false);
        getContentPane().add(level4Btn, Integer.valueOf(1));
        if (flag) {
            level4Btn.setVisible(false);
        }

        JButton level5Btn = new JButton("Level 5");
        level5Btn.setLocation(new Point(530, 155));
        level5Btn.setSize(80, 60);
        level5Btn.setFont(f);
        level5Btn.setForeground(Color.WHITE);
        level5Btn.setMargin(new Insets(0, 0, 0, 0));
        level5Btn.setBorderPainted(false);
        level5Btn.setBorder(null);
        level5Btn.setFocusPainted(false);
        level5Btn.setContentAreaFilled(false);
        getContentPane().add(level5Btn, Integer.valueOf(1));
        if (flag) {
            level5Btn.setVisible(false);
        }

        JButton level6Btn = new JButton("Level 6");
        level6Btn.setLocation(new Point(350, 155));
        level6Btn.setSize(80, 60);
        level6Btn.setFont(f);
        level6Btn.setForeground(Color.WHITE);
        level6Btn.setMargin(new Insets(0, 0, 0, 0));
        level6Btn.setBorderPainted(false);
        level6Btn.setBorder(null);
        level6Btn.setFocusPainted(false);
        level6Btn.setContentAreaFilled(false);
        getContentPane().add(level6Btn, Integer.valueOf(1));
        level6Btn.setVisible(false);
        if(flag) {
            level6Btn.setVisible(true);
        }

        JButton kingBtn = new JButton("King");
        kingBtn.setLocation(new Point(700, 350));
        kingBtn.setSize(80, 60);
        kingBtn.setFont(f);
        kingBtn.setForeground(Color.WHITE);
        kingBtn.setMargin(new Insets(0, 0, 0, 0));
        kingBtn.setBorderPainted(false);
        kingBtn.setBorder(null);
        kingBtn.setFocusPainted(false);
        kingBtn.setContentAreaFilled(false);
        getContentPane().add(kingBtn, Integer.valueOf(1));
        kingBtn.setVisible(false);
        if (!flag && this.user.lv()[4]) {
            kingBtn.setVisible(true);
        }

        JButton backBtn = new JButton("Back");
        backBtn.setLocation(new Point(700, 350));
        backBtn.setSize(80, 60);
        backBtn.setFont(f);
        backBtn.setForeground(Color.WHITE);
        backBtn.setMargin(new Insets(0, 0, 0, 0));
        backBtn.setBorderPainted(false);
        backBtn.setBorder(null);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        getContentPane().add(backBtn, Integer.valueOf(1));
        backBtn.setVisible(false);
        if (flag) {
            backBtn.setVisible(true);
        }

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setLocation(new Point(300, 275));
        logoutBtn.setSize(80, 60);
        logoutBtn.setFont(f);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setMargin(new Insets(0, 0, 0, 0));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setBorder(null);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setContentAreaFilled(false);
        getContentPane().add(logoutBtn, Integer.valueOf(1));

        JButton musicBtn = new JButton("Music");
        musicBtn.setLocation(new Point(400, 275));
        musicBtn.setSize(80, 60);
        musicBtn.setFont(f);
        musicBtn.setForeground(Color.WHITE);
        musicBtn.setMargin(new Insets(0, 0, 0, 0));
        musicBtn.setBorderPainted(false);
        musicBtn.setBorder(null);
        musicBtn.setFocusPainted(false);
        musicBtn.setContentAreaFilled(false);
        getContentPane().add(musicBtn, Integer.valueOf(1));

        JButton changeModeBtn = new JButton("Change Mode");
        changeModeBtn.setLocation(new Point(10, 350));
        changeModeBtn.setSize(140, 60);
        getContentPane().add(changeModeBtn, Integer.valueOf(1));
        changeModeBtn.setFont(f);
        changeModeBtn.setForeground(Color.WHITE);
        changeModeBtn.setMargin(new Insets(0, 0, 0, 0));
        changeModeBtn.setBorderPainted(false);
        changeModeBtn.setBorder(null);
        changeModeBtn.setFocusPainted(false);
        changeModeBtn.setContentAreaFilled(false);

        ImageIcon back;// 背景图置于最底层
        if (this.mode) {
            back = new ImageIcon("src/images/Menu_Theme_Voidheart_Alter.png");
        } else {
            back = new ImageIcon("src/images/Menu_Theme_Voidheart.png");
        }
        back.setImage(back.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
        bg = new JLabel(back);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层


        level1Btn.addActionListener(_ -> {
            this.lv = 1;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_1.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound, this.mode, this.time, this);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level2Btn.addActionListener(_ -> {
            this.lv = 2;
            if (this.user.lv()[this.lv - 2]) {
                MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_2.getMap());
                GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound, this.mode, this.time, this);
                this.setVisible(false);
                gameFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Locked", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        level3Btn.addActionListener(_ -> {
            this.lv = 3;
            if (this.user.lv()[this.lv - 2]) {
                MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_3.getMap());
                GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound, this.mode, this.time, this);
                this.setVisible(false);
                gameFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Locked", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        level4Btn.addActionListener(_ -> {
            this.lv = 4;
            if (this.user.lv()[this.lv - 2]) {
                MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_4.getMap());
                GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound, this.mode, this.time, this);
                this.setVisible(false);
                gameFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Locked", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        level5Btn.addActionListener(_ -> {
            this.lv = 5;
            if (this.user.lv()[this.lv - 2]) {
                MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_5.getMap());
                GameFrame gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound, this.mode, this.time, this);
                this.setVisible(false);
                gameFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Locked", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        level6Btn.addActionListener(_ -> {
            this.lv = 6;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_6.getMap());
            GameFrame gameFrame = new GameFrame(900, 600, mapMatrix, this.user, this.lv, 0, this.sound, this.mode, this.time, this);
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
            this.getContentPane().remove(panel);
            panel = new LevelParticleEffect(50, this.mode);
            panel.setBounds(0, 0, 800, 450);
            panel.setOpaque(false);
            this.getContentPane().add(panel, Integer.valueOf(0));
            if (this.mode) {
                this.getContentPane().remove(bg);
                ImageIcon imageIcon = new ImageIcon("src/images/Menu_Theme_Voidheart_Alter.png");
                imageIcon.setImage(imageIcon.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
                bg = new JLabel(imageIcon);
                bg.setBounds(0, 0, this.getWidth(), this.getHeight());
                this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层
                this.getContentPane().repaint();
            } else {
                this.getContentPane().remove(bg);
                ImageIcon imageIcon = new ImageIcon("src/images/Menu_Theme_Voidheart.png");
                imageIcon.setImage(imageIcon.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
                bg = new JLabel(imageIcon);
                bg.setBounds(0, 0, this.getWidth(), this.getHeight());
                this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层
                this.getContentPane().repaint();
            }
        });

        backBtn.addActionListener(_ -> {
            level6Btn.setVisible(false);
            level1Btn.setVisible(true);
            level2Btn.setVisible(true);
            level3Btn.setVisible(true);
            level4Btn.setVisible(true);
            level5Btn.setVisible(true);
            backBtn.setVisible(false);
            kingBtn.setVisible(true);
        });

        kingBtn.addActionListener(_ -> {
            level1Btn.setVisible(false);
            level2Btn.setVisible(false);
            level3Btn.setVisible(false);
            level4Btn.setVisible(false);
            level5Btn.setVisible(false);
            level6Btn.setVisible(true);
            kingBtn.setVisible(false);
            backBtn.setVisible(true);
        });

        musicBtn.addActionListener(_ -> new MusicFrame(this, this.sound));

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

