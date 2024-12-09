package view.level;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;

import model.Level;
import model.MapMatrix;
import view.ParticlePanel;
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
    private ParticlePanel panel;

    public LevelFrame(User user, Sound sound, boolean mode, boolean flag) {
        this.setTitle("Level");
        this.setLayout(null);
        this.setSize(800, 450);
        this.sound = sound;
        this.time = new Random(System.currentTimeMillis()).nextInt(121) + 30;
        this.setResizable(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.setContentPane(layeredPane);

        panel = new ParticlePanel(50, mode, true);
        panel.setBounds(0, 0, 800, 450);
        panel.setOpaque(false);
        this.getContentPane().add(panel, Integer.valueOf(0));

        Font f = new Font("Comic Sans MS", Font.BOLD, 16);
        this.mode = mode;
        this.user = user;

        ImageIcon starImg = new ImageIcon("src/images/star.png");
        starImg.setImage(starImg.getImage().getScaledInstance(20, 15, Image.SCALE_DEFAULT));
        ImageIcon crownImg = new ImageIcon("src/images/crown.png");
        crownImg.setImage(crownImg.getImage().getScaledInstance(40, 30, Image.SCALE_DEFAULT));
        ImageIcon kingImg = new ImageIcon("src/images/king.png");
        kingImg.setImage(kingImg.getImage().getScaledInstance(40, 30, Image.SCALE_DEFAULT));
        ImageIcon kingAltImg = new ImageIcon("src/images/king_alter.png");
        kingAltImg.setImage(kingAltImg.getImage().getScaledInstance(40, 30, Image.SCALE_DEFAULT));
        ImageIcon lockImg = new ImageIcon("src/images/lock.png");
        lockImg.setImage(lockImg.getImage().getScaledInstance(80, 45, Image.SCALE_DEFAULT));

        JLabel[][] stars = new JLabel[3][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                stars[j][i] = new JLabel(starImg);
                stars[j][i].setVisible(false);
            }
            stars[0][i].setBounds(170 + i * 90, 230, 40, 30);
            stars[1][i].setBounds(210 + i * 90, 230, 40, 30);
            stars[2][i].setBounds(190 + i * 90, 210, 40, 30);
        }

        JLabel[] locks = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            locks[i] = new JLabel(lockImg);
            locks[i].setVisible(true);
            locks[i].setBounds(260 + i * 90, 180, 80, 45);
        }

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
        level1Btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                level1Btn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                level1Btn.setForeground(Color.WHITE);
            }
        });
        getContentPane().add(level1Btn, Integer.valueOf(1));
        for (int i = 0; i < 3; i++) {
            this.getContentPane().add(stars[i][0], Integer.valueOf(1));
        }
        show(0, stars);
        if (flag) {
            level1Btn.setVisible(false);
            noShow(0, stars);
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
        level2Btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                level2Btn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                level2Btn.setForeground(Color.WHITE);
            }
        });
        getContentPane().add(level2Btn, Integer.valueOf(1));
        getContentPane().add(locks[0], Integer.valueOf(2));
        for (int i = 0; i < 3; i++) {
            this.getContentPane().add(stars[i][1], Integer.valueOf(1));
        }
        if (user.getLv()[0][0]) {
            show(1, stars);
            locks[0].setVisible(false);
        }

        if (flag) {
            level2Btn.setVisible(false);
            noShow(1, stars);
            locks[0].setVisible(false);
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
        getContentPane().add(locks[1], Integer.valueOf(2));
        level3Btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                level3Btn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                level3Btn.setForeground(Color.WHITE);
            }
        });
        for (int i = 0; i < 3; i++) {
            this.getContentPane().add(stars[i][2], Integer.valueOf(1));
        }
        if (user.getLv()[0][1]) {
            show (2, stars);
            locks[1].setVisible(false);
        }
        if (flag) {
            level3Btn.setVisible(false);
            noShow(2, stars);
            locks[1].setVisible(false);
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
        level4Btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                level4Btn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                level4Btn.setForeground(Color.WHITE);
            }
        });
        getContentPane().add(level4Btn, Integer.valueOf(1));
        getContentPane().add(locks[2], Integer.valueOf(2));
        for (int i = 0; i < 3; i++) {
            this.getContentPane().add(stars[i][3], Integer.valueOf(1));
        }
        if (user.getLv()[0][2]) {
            show (3, stars);
            locks[2].setVisible(false);
        }
        if (flag) {
            level4Btn.setVisible(false);
            noShow(3, stars);
            locks[2].setVisible(false);
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
        level5Btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                level5Btn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                level5Btn.setForeground(Color.WHITE);
            }
        });
        getContentPane().add(level5Btn, Integer.valueOf(1));
        getContentPane().add(locks[3], Integer.valueOf(2));
        for (int i = 0; i < 3; i++) {
           this.getContentPane().add(stars[i][4], Integer.valueOf(1));
        }

        if (user.getLv()[0][3]) {
            show(4, stars);
            locks[3].setVisible(false);
        }
        if (flag) {
            level5Btn.setVisible(false);
            noShow(4, stars);
            locks[3].setVisible(false);
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
        level6Btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                level6Btn.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                level6Btn.setForeground(Color.WHITE);
            }
        });
        getContentPane().add(level6Btn, Integer.valueOf(1));
        level6Btn.setVisible(false);

        JLabel crown = new JLabel(crownImg);
        crown.setBounds(370, 230, 40, 30);
        this.getContentPane().add(crown, Integer.valueOf(1));
        crown.setVisible(false);

        if (flag) {
            level6Btn.setVisible(true);
            crown.setVisible(true);
        }

        JButton kingBtn = new JButton(kingImg);
        kingBtn.setLocation(new Point(700, 350));
        kingBtn.setSize(80, 60);
        kingBtn.setMargin(new Insets(0, 0, 0, 0));
        kingBtn.setBorderPainted(false);
        kingBtn.setBorder(null);
        kingBtn.setFocusPainted(false);
        kingBtn.setContentAreaFilled(false);
        kingBtn.setVisible(false);
        getContentPane().add(kingBtn, Integer.valueOf(1));
        if (!flag && this.user.getLv()[0][4]) {
            kingBtn.setVisible(true);
        }

        JButton backBtn = new JButton(kingAltImg);
        backBtn.setLocation(new Point(700, 350));
        backBtn.setSize(80, 60);
        backBtn.setFont(f);
        backBtn.setForeground(Color.WHITE);
        backBtn.setMargin(new Insets(0, 0, 0, 0));
        backBtn.setBorderPainted(false);
        backBtn.setBorder(null);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backBtn.setForeground(Color.WHITE);
            }
        });
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
        logoutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutBtn.setForeground(Color.WHITE);
            }
        });
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
        musicBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                musicBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                musicBtn.setForeground(Color.WHITE);
            }
        });
        getContentPane().add(musicBtn, Integer.valueOf(1));

        JButton changeModeBtn = new JButton("Change Mode");
        changeModeBtn.setLocation(new Point(10, 350));
        changeModeBtn.setSize(140, 60);
        changeModeBtn.setFont(f);
        changeModeBtn.setForeground(Color.WHITE);
        changeModeBtn.setMargin(new Insets(0, 0, 0, 0));
        changeModeBtn.setBorderPainted(false);
        changeModeBtn.setBorder(null);
        changeModeBtn.setFocusPainted(false);
        changeModeBtn.setContentAreaFilled(false);
        changeModeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                changeModeBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                changeModeBtn.setForeground(Color.WHITE);
            }
        });
        getContentPane().add(changeModeBtn, Integer.valueOf(1));

        JButton settingBtn = new JButton("Account Setting");
        settingBtn.setLocation(new Point(600, 10));
        settingBtn.setSize(140, 60);
        settingBtn.setFont(f);
        settingBtn.setForeground(Color.WHITE);
        settingBtn.setMargin(new Insets(0, 0, 0, 0));
        settingBtn.setBorderPainted(false);
        settingBtn.setBorder(null);
        settingBtn.setFocusPainted(false);
        settingBtn.setContentAreaFilled(false);
        settingBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                settingBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                settingBtn.setForeground(Color.WHITE);
            }
        });
        getContentPane().add(settingBtn, Integer.valueOf(1));
        if (this.user.getId() == 0) {
            settingBtn.setVisible(false);
        }

        ImageIcon backImg;// 背景图置于最底层
        if (this.mode) {
            backImg = new ImageIcon("src/images/Menu_Theme_Voidheart_Alter.png");
        } else {
            backImg = new ImageIcon("src/images/Menu_Theme_Voidheart.png");
        }
        backImg.setImage(backImg.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
        bg = new JLabel(backImg);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层

        level1Btn.addActionListener(_ -> {
            this.lv = 1;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_1.getMap());
            GameFrame gameFrame = new GameFrame(800, 450, this, mapMatrix, this.user, this.sound, this.lv, 0, this.mode, this.time);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        level2Btn.addActionListener(_ -> {
            this.lv = 2;
            if (this.user.getLv()[0][this.lv - 2]) {
                MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_2.getMap());
                GameFrame gameFrame = new GameFrame(800, 450, this, mapMatrix, this.user, this.sound, this.lv, 0, this.mode, this.time);
                this.setVisible(false);
                gameFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Locked", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        level3Btn.addActionListener(_ -> {
            this.lv = 3;
            if (this.user.getLv()[0][this.lv - 2]) {
                MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_3.getMap());
                GameFrame gameFrame = new GameFrame(800, 450, this, mapMatrix, this.user, this.sound, this.lv, 0, this.mode, this.time);
                this.setVisible(false);
                gameFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Locked", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        level4Btn.addActionListener(_ -> {
            this.lv = 4;
            if (this.user.getLv()[0][this.lv - 2]) {
                MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_4.getMap());
                GameFrame gameFrame = new GameFrame(800, 450, this, mapMatrix, this.user, this.sound, this.lv, 0, this.mode, this.time);
                this.setVisible(false);
                gameFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Locked", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        level5Btn.addActionListener(_ -> {
            this.lv = 5;
            if (this.user.getLv()[0][this.lv - 2]) {
                MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_5.getMap());
                GameFrame gameFrame = new GameFrame(800, 450, this, mapMatrix, this.user, this.sound, this.lv, 0, this.mode, this.time);
                this.setVisible(false);
                gameFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Locked", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        level6Btn.addActionListener(_ -> {
            this.lv = 6;
            MapMatrix mapMatrix = new MapMatrix(Level.LEVEL_6.getMap());
            GameFrame gameFrame = new GameFrame(900, 600, this, mapMatrix, this.user, this.sound, this.lv, 0, this.mode, this.time);
            this.setVisible(false);
            gameFrame.setVisible(true);
        });

        logoutBtn.addActionListener(_ -> {
            ImageIcon originalIcon = new ImageIcon("src/images/Logout.png");
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(450, 450, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            int option = JOptionPane.showOptionDialog(this, null, "CONFIRM", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, resizedIcon, new Object[]{"Cancel", "Confirm"}, "Cancel");
            if (option == 1) {
                this.setVisible(false);
                LoginFrame loginFrame = new LoginFrame(this.sound);
                loginFrame.setVisible(true);
            }
        });

        changeModeBtn.addActionListener(_ -> {
            //change bg
            this.mode = !this.mode;
            System.out.println("change to mode " + this.mode);
            this.getContentPane().remove(panel);
            panel = new ParticlePanel(50, this.mode, true);
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

        settingBtn.addActionListener(_ -> new SettingFrame(this, this.user, this.sound));

        backBtn.addActionListener(_ -> {
            level6Btn.setVisible(false);
            level1Btn.setVisible(true);
            level2Btn.setVisible(true);
            level3Btn.setVisible(true);
            level4Btn.setVisible(true);
            level5Btn.setVisible(true);
            backBtn.setVisible(false);
            kingBtn.setVisible(true);
            for (int i = 0; i < 5; i++) {
                show(i, stars);
            }
            crown.setVisible(false);
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
            for (int i = 0; i < 5; i++) {
                noShow(i, stars);
            }
            crown.setVisible(true);
        });

        musicBtn.addActionListener(_ -> new MusicFrame(this, this.sound));

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void show(int k, JLabel[][] stars) {
        for (int i = 0; i < 3; i++) {
            if (user.getLv()[i][k]) {
                stars[i][k].setVisible(true);
            }
        }
    }

    public void noShow(int k, JLabel[][] stars) {
        for (int i = 0; i < 3; i++) {
            stars[i][k].setVisible(false);
        }
    }
}

