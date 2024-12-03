package view.game;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import controller.GameController;
import model.MapMatrix;
import view.FileSHAUtil;
import view.level.LevelFrame;
import view.login.User;
import view.music.MusicFrame;
import view.music.Sound;

public class GameFrame extends JFrame {

    private final GameController controller;
    private final GamePanel gamePanel;
    private final int lv;
    private final Sound sound;
    private final User user;
    private final FileFrame fileFrame;
    private final boolean mode;
    private final int time;
    private final int[] leastStep = {13, 23, 31, 27, 37};
    JLabel leftTimeLabel;
    JLabel timeLabel;
    String musicPath;
    JButton backBtn;
    private LevelFrame levelFrame;
    private boolean check = true;

    public GameFrame(int width, int height, MapMatrix mapMatrix, User user, int lv, int step, Sound sound, boolean mode, int time, LevelFrame levelFrame) {
        Logger log = Logger.getLogger(GameFrame.class.getName());
        try {
            String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        Font font = new Font("Arial", Font.BOLD, 25);
        Font f = new Font("Comic Sans MS", Font.PLAIN, 22);
        Font f2 = new Font("Comic Sans MS", Font.PLAIN, 18);
        this.mode = mode;
        int temp = 0;
        if (this.mode) {
            temp = time;
        }
        this.time = temp;
        this.lv = lv;
        this.setTitle(String.format("Level %d", this.lv));
        this.setLayout(null);
        this.setSize(width, height);
        this.user = user;
        this.sound = sound;
        this.setResizable(false);
        this.levelFrame = levelFrame;
        musicPath = this.sound.getMusicPath();
        String filepath = String.format("src/saves/%d-%d.json", this.lv, user.id());
        File file = new File(filepath);
        gamePanel = new GamePanel(mapMatrix, this, this.user, step);
        gamePanel.setFocusable(true);
        gamePanel.setLocation(30, height / 2 - gamePanel.getHeight() / 2);
        this.getContentPane().add(gamePanel);
        this.controller = new GameController(gamePanel, mapMatrix, this.user, this.lv, this.sound, this.levelFrame);
        this.fileFrame = new FileFrame(800, 450, this.user, this, this.lv, this.sound);

        SwingUtilities.invokeLater(gamePanel::requestFocusInWindow);//为什么这里不能换成this.gamePanel.requestFocusInWindow()

        JButton restartBtn = new JButton("Restart");
        restartBtn.setLocation(new Point(gamePanel.getWidth() + 80, 120));
        restartBtn.setSize(80, 50);
        restartBtn.setFont(f2);
        restartBtn.setMargin(new Insets(0, 0, 0, 0));
        restartBtn.setBorderPainted(false);
        restartBtn.setBorder(null);
        restartBtn.setFocusPainted(false);
        restartBtn.setContentAreaFilled(false);
        restartBtn.setForeground(Color.WHITE);
        this.getContentPane().add(restartBtn);

        JButton loadBtn = new JButton("Saving");
        loadBtn.setLocation(new Point(gamePanel.getWidth() + 180, 180));
        loadBtn.setSize(80, 50);
        loadBtn.setFont(f2);
        loadBtn.setMargin(new Insets(0, 0, 0, 0));
        loadBtn.setBorderPainted(false);
        loadBtn.setBorder(null);
        loadBtn.setFocusPainted(false);
        loadBtn.setContentAreaFilled(false);
        loadBtn.setForeground(Color.WHITE);
        this.getContentPane().add(loadBtn);

        backBtn = new JButton("Back");
        backBtn.setLocation(new Point(gamePanel.getWidth() + 80, 240));
        backBtn.setSize(80, 50);
        backBtn.setFont(f2);
        backBtn.setMargin(new Insets(0, 0, 0, 0));
        backBtn.setBorderPainted(false);
        backBtn.setBorder(null);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setForeground(Color.WHITE);
        this.getContentPane().add(backBtn);

        JButton helpBtn = new JButton("Help");
        helpBtn.setLocation(new Point(gamePanel.getWidth() + 80, 300));
        helpBtn.setSize(80, 50);
        helpBtn.setFont(f2);
        helpBtn.setMargin(new Insets(0, 0, 0, 0));
        helpBtn.setBorderPainted(false);
        helpBtn.setBorder(null);
        helpBtn.setFocusPainted(false);
        helpBtn.setContentAreaFilled(false);
        helpBtn.setForeground(Color.WHITE);
        this.getContentPane().add(helpBtn);

        JButton musicBtn = new JButton("Music");
        musicBtn.setLocation(new Point(gamePanel.getWidth() + 180, 120));
        musicBtn.setSize(80, 50);
        musicBtn.setFont(f2);
        musicBtn.setMargin(new Insets(0, 0, 0, 0));
        musicBtn.setBorderPainted(false);
        musicBtn.setBorder(null);
        musicBtn.setFocusPainted(false);
        musicBtn.setContentAreaFilled(false);
        musicBtn.setForeground(Color.WHITE);
        this.getContentPane().add(musicBtn);

        JButton undoBtn = new JButton("Undo");
        undoBtn.setLocation(new Point(gamePanel.getWidth() + 80, 180));
        undoBtn.setSize(80, 50);
        undoBtn.setFont(f2);
        undoBtn.setMargin(new Insets(0, 0, 0, 0));
        undoBtn.setBorderPainted(false);
        undoBtn.setBorder(null);
        undoBtn.setFocusPainted(false);
        undoBtn.setContentAreaFilled(false);
        undoBtn.setForeground(Color.WHITE);
        this.getContentPane().add(undoBtn);

        JButton upMoveBtn = new JButton("↑");
        upMoveBtn.setLocation(new Point(gamePanel.getWidth() + 220, 260));
        upMoveBtn.setSize(30, 30);
        upMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        upMoveBtn.setBorderPainted(false);
        upMoveBtn.setBorder(null);
        upMoveBtn.setFocusPainted(false);
        upMoveBtn.setContentAreaFilled(false);
        upMoveBtn.setFont(font);
        upMoveBtn.setForeground(Color.WHITE);
        this.getContentPane().add(upMoveBtn);

        JButton downMoveBtn = new JButton("↓");
        downMoveBtn.setLocation(new Point(gamePanel.getWidth() + 220, 320));
        downMoveBtn.setSize(30, 30);
        downMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        downMoveBtn.setBorderPainted(false);
        downMoveBtn.setBorder(null);
        downMoveBtn.setFocusPainted(false);
        downMoveBtn.setContentAreaFilled(false);
        downMoveBtn.setFont(font);
        downMoveBtn.setForeground(Color.WHITE);
        this.getContentPane().add(downMoveBtn);

        JButton leftMoveBtn = new JButton("←");
        leftMoveBtn.setLocation(new Point(gamePanel.getWidth() + 190, 290));
        leftMoveBtn.setSize(30, 30);
        leftMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        leftMoveBtn.setBorderPainted(false);
        leftMoveBtn.setBorder(null);
        leftMoveBtn.setFocusPainted(false);
        leftMoveBtn.setContentAreaFilled(false);
        leftMoveBtn.setFont(font);
        leftMoveBtn.setForeground(Color.WHITE);
        this.getContentPane().add(leftMoveBtn);

        JButton rightMoveBtn = new JButton("→");
        rightMoveBtn.setLocation(new Point(gamePanel.getWidth() + 250, 290));
        rightMoveBtn.setSize(30, 30);
        rightMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        rightMoveBtn.setBorderPainted(false);
        rightMoveBtn.setBorder(null);
        rightMoveBtn.setFocusPainted(false);
        rightMoveBtn.setContentAreaFilled(false);
        rightMoveBtn.setFont(font);
        rightMoveBtn.setForeground(Color.WHITE);
        this.getContentPane().add(rightMoveBtn);

        if (lv != 6) {
            helpBtn.setVisible(false);
            JLabel leastStepLabel = new JLabel(String.format("Min_Steps: %d", leastStep[lv - 1]));
            leastStepLabel.setFont(f);
            leastStepLabel.setLocation(new Point(gamePanel.getWidth() + 200, 70));
            leastStepLabel.setSize(180, 50);
            getContentPane().add(leastStepLabel, Integer.valueOf(1));
            leastStepLabel.setForeground(Color.WHITE);

            JLabel lvLabel = new JLabel(String.format("Level: %d", this.lv));
            lvLabel.setFont(f);
            lvLabel.setLocation(new Point(gamePanel.getWidth() + 80, 20));
            lvLabel.setSize(180, 50);
            getContentPane().add(lvLabel, Integer.valueOf(1));
            lvLabel.setForeground(Color.WHITE);

            if (isMode()) {
                leftTimeLabel = new JLabel(String.format("Left time: %d", this.time));
                leftTimeLabel.setFont(f);
                leftTimeLabel.setLocation(new Point(gamePanel.getWidth() + 200, 20));
                leftTimeLabel.setSize(180, 50);
                getContentPane().add(leftTimeLabel, Integer.valueOf(1));
                leftTimeLabel.setForeground(Color.WHITE);
            } else {
                timeLabel = new JLabel(String.format("Time: %d", this.time));
                timeLabel.setFont(f);
                timeLabel.setLocation(new Point(gamePanel.getWidth() + 200, 20));
                timeLabel.setSize(180, 50);
                getContentPane().add(timeLabel, Integer.valueOf(1));
                timeLabel.setForeground(Color.WHITE);
            }

            JLabel stepLabel = new JLabel(String.format("Step: %d", step));
            stepLabel.setFont(f);
            stepLabel.setLocation(new Point(gamePanel.getWidth() + 80, 70));
            stepLabel.setSize(180, 50);
            getContentPane().add(stepLabel, Integer.valueOf(1));
            stepLabel.setForeground(Color.WHITE);
            gamePanel.setStepLabel(stepLabel);
        } else {
            loadBtn.setVisible(false);
            musicBtn.setVisible(false);
            this.sound.changeSource("src/misc/东方永夜抄竹取飞翔.wav");
            sound.setVolume(0.5);
            sound.play();

            JLabel leastStepLabel = new JLabel("Min_Steps: ???");
            leastStepLabel.setFont(f);
            leastStepLabel.setLocation(new Point(gamePanel.getWidth() + 200, 70));
            leastStepLabel.setSize(180, 50);
            getContentPane().add(leastStepLabel, Integer.valueOf(1));
            leastStepLabel.setForeground(Color.WHITE);

            JLabel lvLabel = new JLabel("Level: ???");
            lvLabel.setFont(f);
            lvLabel.setLocation(new Point(gamePanel.getWidth() + 80, 20));
            lvLabel.setSize(180, 50);
            getContentPane().add(lvLabel, Integer.valueOf(1));
            lvLabel.setForeground(Color.WHITE);

            if (isMode()) {
                leftTimeLabel = new JLabel("Left time: ???");
                leftTimeLabel.setFont(f);
                leftTimeLabel.setLocation(new Point(gamePanel.getWidth() + 200, 20));
                leftTimeLabel.setSize(180, 50);
                getContentPane().add(leftTimeLabel, Integer.valueOf(1));
                leftTimeLabel.setForeground(Color.WHITE);
            } else {
                timeLabel = new JLabel("Time: ???");
                timeLabel.setFont(f);
                timeLabel.setLocation(new Point(gamePanel.getWidth() + 200, 20));
                timeLabel.setSize(180, 50);
                getContentPane().add(timeLabel, Integer.valueOf(1));
                timeLabel.setForeground(Color.WHITE);
            }

            JLabel stepLabel = new JLabel("Step: ???");
            stepLabel.setFont(f);
            stepLabel.setLocation(new Point(gamePanel.getWidth() + 80, 70));
            stepLabel.setSize(180, 50);
            getContentPane().add(stepLabel, Integer.valueOf(1));
            stepLabel.setForeground(Color.WHITE);
            gamePanel.setStepLabel(stepLabel);
        }

        restartBtn.addActionListener(_ -> {
            controller.restartGame();
            gamePanel.requestFocusInWindow();//enable key listener
        });

        loadBtn.addActionListener(_ -> {
            if (isMode() || lv == 6) {
                JOptionPane.showMessageDialog(this, "此模式无法存档");
                gamePanel.requestFocusInWindow();
                return;
            }
            controller.getTimer().stop();
            if (this.user.id() == 0) {
                JOptionPane.showMessageDialog(this, "游客模式不能存档喵~", "QAQ", JOptionPane.ERROR_MESSAGE);
                controller.getTimer().start();
                gamePanel.requestFocusInWindow();
            } else {
                if (check) {
                    Sound s = new Sound("src/misc/zako.wav");
                    s.setVolume(0.8);
                    s.play();
                    JOptionPane.showOptionDialog(this, "不会要用存档才能过吧~ 雑魚♡~ 雑魚♡~", "雑魚♡~", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"我是杂鱼喵~", "私は雑魚にゃ♡~"}, "私は雑魚にゃ♡~");
                    check = false;
                }
                fileFrame.Show(0);
                this.setVisible(false);
                fileFrame.setVisible(true);
                gamePanel.requestFocusInWindow();
            }
        });

        backBtn.addActionListener(_ -> {
            if (this.getLv() == 6) {
                this.sound.changeSource(musicPath);
                sound.setVolume(0.5);
                sound.play();
            }
            controller.getTimer().stop();
            if (this.lv == 6) {
                this.levelFrame = new LevelFrame(this.user, this.sound, this.mode, true);
            } else {
                this.levelFrame = new LevelFrame(this.user, this.sound, this.mode, false);
            }
            this.levelFrame.setVisible(true);
            this.dispose();
        });

        undoBtn.addActionListener(_ -> {
            if (gamePanel.getSteps() == 0) {
                JOptionPane.showMessageDialog(this, "步数为0，无法撤回", "Error", JOptionPane.INFORMATION_MESSAGE);
                gamePanel.requestFocusInWindow();
            } else {
                gamePanel.undoMove();
                gamePanel.requestFocusInWindow();
            }
        });

        musicBtn.addActionListener(_ -> {
            new MusicFrame(this, this.sound);
            gamePanel.requestFocusInWindow();
        });

        helpBtn.addActionListener(_ -> {
            ImageIcon originalIcon = new ImageIcon("src/images/Help.png");
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(1200, 675, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            JOptionPane.showMessageDialog(this, null, "Help", JOptionPane.INFORMATION_MESSAGE, resizedIcon);
            gamePanel.requestFocusInWindow();
        });

        upMoveBtn.addActionListener(_ -> {
            gamePanel.doMoveUp();
            gamePanel.requestFocusInWindow();//enable key listener
        });

        downMoveBtn.addActionListener(_ -> {
            gamePanel.doMoveDown();
            gamePanel.requestFocusInWindow();//enable key listener
        });

        leftMoveBtn.addActionListener(_ -> {
            gamePanel.doMoveLeft();
            gamePanel.requestFocusInWindow();//enable key listener
        });

        rightMoveBtn.addActionListener(_ -> {
            gamePanel.doMoveRight();
            gamePanel.requestFocusInWindow();//enable key listener
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ImageIcon back = new ImageIcon("src/images/Menu_Theme_The_Eternal_Ordeal.png");
        back.setImage(back.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        JLabel bg = new JLabel(back);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层

        if (!file.exists() && user.id() != 0 && lv != 6) {
            if (!file.getParentFile().mkdirs()) {
                System.err.println("目录已存在喵: " + file.getParentFile().getAbsolutePath());
            }
            MapInfo mapInfo = new MapInfo();
            System.out.println(mapInfo.getId());
            mapInfo.setModel(controller.getModel());
            try {
                FileFrame.createFile(filepath);
                for (int i = 0; i < 6; i++) {
                    MapInfo mapInfo2 = new MapInfo();
                    mapInfo2.setModel(null);
                    mapInfo2.setId(i);
                    mapInfo2.setStep(0);
                    FileFrame.addNewMap(mapInfo2, filepath);
                }
                System.out.println("创建新文件并保存");
            } catch (Exception e) {
                System.err.println("保存失败");
                log.info(e.getMessage());
            }
            try {
                boolean result = FileFrame.updateMapById(0, controller.getModel(), this.gamePanel.getSteps(), this.gamePanel.getMoveHero(), this.gamePanel.getMoveBox(), this.gamePanel.getTime(), filepath);
                if (result) {
                    System.out.println("更新成功");
                } else {
                    System.err.println("更新失败");
                }
                FileSHAUtil.saveSHAToFile(FileSHAUtil.calculateSHA(new File(filepath)), new File(filepath + ".sha"));
            } catch (IOException e) {
                log.info(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        File shaFile = new File(filepath + ".sha");
        if (!shaFile.exists() && user.id() != 0 && lv != 6) {
            System.err.println("存档文档损坏喵！");
            if (file.delete()) {
                System.err.println("存档已清空！！！");
                MapInfo mapInfo = new MapInfo();
                System.out.println(mapInfo.getId());
                mapInfo.setModel(controller.getModel());
                try {
                    FileFrame.createFile(filepath);
                    for (int i = 0; i < 6; i++) {
                        MapInfo mapInfo2 = new MapInfo();
                        mapInfo2.setModel(null);
                        mapInfo2.setId(i);
                        mapInfo2.setStep(0);
                        FileFrame.addNewMap(mapInfo2, filepath);
                    }
                    System.out.println("创建新文件并保存");
                } catch (Exception e) {
                    System.err.println("保存失败");
                    log.info(e.getMessage());
                }
                try {
                    boolean result = FileFrame.updateMapById(0, controller.getModel(), this.gamePanel.getSteps(), this.gamePanel.getMoveHero(), this.gamePanel.getMoveBox(), this.gamePanel.getTime(), filepath);
                    if (result) {
                        System.out.println("更新成功");
                    } else {
                        System.out.println("更新失败");
                    }
                    FileSHAUtil.saveSHAToFile(FileSHAUtil.calculateSHA(new File(filepath)), new File(filepath + ".sha"));
                } catch (IOException e) {
                    log.info(e.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public JButton getBackBtn() {
        return backBtn;
    }

    public Sound getSound() {
        return sound;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    public LevelFrame getLevelFrame() {
        return levelFrame;
    }

    public User getUser() {
        return user;
    }

    public JLabel getLeftTimeLabel() {
        return leftTimeLabel;
    }

    public FileFrame getFileFrame() {
        return fileFrame;
    }

    public int getLv() {
        return lv;
    }

    public GameController getGameController() {
        return controller;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public GameController getController() {
        return controller;
    }

    public boolean isMode() {
        return mode;
    }

    public int getTime() {
        return time;
    }

    public int[] getLeastStep() {
        return leastStep;
    }
}
