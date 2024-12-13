package view.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private final String musicPath;
    private final JButton backBtn;
    private JLabel leftTimeLabel;
    private JLabel timeLabel;
    private LevelFrame levelFrame;
    private boolean check = true;

    public GameFrame(int width, int height, LevelFrame levelFrame, MapMatrix mapMatrix, User user, Sound sound, int lv, int step, boolean mode, int time) {
        Logger log = Logger.getLogger(GameFrame.class.getName());

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
        this.user = user;
        this.sound = sound;
        this.levelFrame = levelFrame;
        this.musicPath = this.sound.getMusicPath();

        this.setTitle(String.format("Level %d", this.lv));
        this.setLayout(null);
        this.setSize(width, height);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        String filepath = String.format("saves/%d-%d.json", this.lv, user.getId());
        File file = new File(filepath);

        this.gamePanel = new GamePanel(mapMatrix, this, this.user, step);
        this.gamePanel.setFocusable(true);
        this.gamePanel.setLocation(30, height / 2 - this.gamePanel.getHeight() / 2);
        this.getContentPane().add(this.gamePanel);

        this.controller = new GameController(this.gamePanel, mapMatrix, this.user, this.lv, this.sound, this.levelFrame);
        this.fileFrame = new FileFrame(800, 450, this.user, this, this.lv, this.sound);

        SwingUtilities.invokeLater(this.gamePanel::requestFocusInWindow);//这里不能换成this.gamePanel.requestFocusInWindow()，神奇。

        JButton restartBtn = new JButton("Restart");
        restartBtn.setLocation(new Point(this.gamePanel.getWidth() + 80, 120));
        restartBtn.setSize(80, 50);
        restartBtn.setFont(f2);
        restartBtn.setMargin(new Insets(0, 0, 0, 0));
        restartBtn.setBorderPainted(false);
        restartBtn.setBorder(null);
        restartBtn.setFocusPainted(false);
        restartBtn.setContentAreaFilled(false);
        restartBtn.setForeground(Color.WHITE);
        restartBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                restartBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                restartBtn.setForeground(Color.WHITE);
            }
        });
        this.getContentPane().add(restartBtn);

        JButton loadBtn = new JButton("Saving");
        loadBtn.setLocation(new Point(this.gamePanel.getWidth() + 180, 180));
        loadBtn.setSize(80, 50);
        loadBtn.setFont(f2);
        loadBtn.setMargin(new Insets(0, 0, 0, 0));
        loadBtn.setBorderPainted(false);
        loadBtn.setBorder(null);
        loadBtn.setFocusPainted(false);
        loadBtn.setContentAreaFilled(false);
        loadBtn.setForeground(Color.WHITE);
        loadBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loadBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loadBtn.setForeground(Color.WHITE);
            }
        });
        this.getContentPane().add(loadBtn);

        this.backBtn = new JButton("Back");
        this.backBtn.setLocation(new Point(this.gamePanel.getWidth() + 80, 240));
        this.backBtn.setSize(80, 50);
        this.backBtn.setFont(f2);
        this.backBtn.setMargin(new Insets(0, 0, 0, 0));
        this.backBtn.setBorderPainted(false);
        this.backBtn.setBorder(null);
        this.backBtn.setFocusPainted(false);
        this.backBtn.setContentAreaFilled(false);
        this.backBtn.setForeground(Color.WHITE);
        this.backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backBtn.setForeground(Color.WHITE);
            }
        });
        this.getContentPane().add(this.backBtn);

        JButton helpBtn = new JButton("Help");
        helpBtn.setLocation(new Point(this.gamePanel.getWidth() + 80, 300));
        helpBtn.setSize(80, 50);
        helpBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        helpBtn.setMargin(new Insets(0, 0, 0, 0));
        helpBtn.setBorderPainted(false);
        helpBtn.setBorder(null);
        helpBtn.setFocusPainted(false);
        helpBtn.setContentAreaFilled(false);
        helpBtn.setForeground(Color.YELLOW);
        helpBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                helpBtn.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                helpBtn.setForeground(Color.YELLOW);
            }
        });
        this.getContentPane().add(helpBtn);

        JButton musicBtn = new JButton("Music");
        musicBtn.setLocation(new Point(this.gamePanel.getWidth() + 180, 120));
        musicBtn.setSize(80, 50);
        musicBtn.setFont(f2);
        musicBtn.setMargin(new Insets(0, 0, 0, 0));
        musicBtn.setBorderPainted(false);
        musicBtn.setBorder(null);
        musicBtn.setFocusPainted(false);
        musicBtn.setContentAreaFilled(false);
        musicBtn.setForeground(Color.WHITE);
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
        this.getContentPane().add(musicBtn);

        JButton undoBtn = new JButton("Undo");
        undoBtn.setLocation(new Point(this.gamePanel.getWidth() + 80, 180));
        undoBtn.setSize(80, 50);
        undoBtn.setFont(f2);
        undoBtn.setMargin(new Insets(0, 0, 0, 0));
        undoBtn.setBorderPainted(false);
        undoBtn.setBorder(null);
        undoBtn.setFocusPainted(false);
        undoBtn.setContentAreaFilled(false);
        undoBtn.setForeground(Color.WHITE);
        undoBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                undoBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                undoBtn.setForeground(Color.WHITE);
            }
        });
        this.getContentPane().add(undoBtn);

        JButton upMoveBtn = new JButton("↑");
        upMoveBtn.setLocation(new Point(this.gamePanel.getWidth() + 220, 260));
        upMoveBtn.setSize(30, 30);
        upMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        upMoveBtn.setBorderPainted(false);
        upMoveBtn.setBorder(null);
        upMoveBtn.setFocusPainted(false);
        upMoveBtn.setContentAreaFilled(false);
        upMoveBtn.setFont(font);
        upMoveBtn.setForeground(Color.WHITE);
        upMoveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                upMoveBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                upMoveBtn.setForeground(Color.WHITE);
            }
        });
        this.getContentPane().add(upMoveBtn);

        JButton downMoveBtn = new JButton("↓");
        downMoveBtn.setLocation(new Point(this.gamePanel.getWidth() + 220, 320));
        downMoveBtn.setSize(30, 30);
        downMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        downMoveBtn.setBorderPainted(false);
        downMoveBtn.setBorder(null);
        downMoveBtn.setFocusPainted(false);
        downMoveBtn.setContentAreaFilled(false);
        downMoveBtn.setFont(font);
        downMoveBtn.setForeground(Color.WHITE);
        downMoveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                downMoveBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                downMoveBtn.setForeground(Color.WHITE);
            }
        });
        this.getContentPane().add(downMoveBtn);

        JButton leftMoveBtn = new JButton("←");
        leftMoveBtn.setLocation(new Point(this.gamePanel.getWidth() + 190, 290));
        leftMoveBtn.setSize(30, 30);
        leftMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        leftMoveBtn.setBorderPainted(false);
        leftMoveBtn.setBorder(null);
        leftMoveBtn.setFocusPainted(false);
        leftMoveBtn.setContentAreaFilled(false);
        leftMoveBtn.setFont(font);
        leftMoveBtn.setForeground(Color.WHITE);
        leftMoveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                leftMoveBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                leftMoveBtn.setForeground(Color.WHITE);
            }
        });
        this.getContentPane().add(leftMoveBtn);

        JButton rightMoveBtn = new JButton("→");
        rightMoveBtn.setLocation(new Point(this.gamePanel.getWidth() + 250, 290));
        rightMoveBtn.setSize(30, 30);
        rightMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        rightMoveBtn.setBorderPainted(false);
        rightMoveBtn.setBorder(null);
        rightMoveBtn.setFocusPainted(false);
        rightMoveBtn.setContentAreaFilled(false);
        rightMoveBtn.setFont(font);
        rightMoveBtn.setForeground(Color.WHITE);
        rightMoveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                rightMoveBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                rightMoveBtn.setForeground(Color.WHITE);
            }
        });
        this.getContentPane().add(rightMoveBtn);

        if (lv != 6) {
            helpBtn.setVisible(false);
            JLabel leastStepLabel = new JLabel(String.format("Min_Steps: %d", this.leastStep[lv - 1]));
            leastStepLabel.setFont(f);
            leastStepLabel.setLocation(new Point(this.gamePanel.getWidth() + 200, 70));
            leastStepLabel.setSize(180, 50);
            leastStepLabel.setForeground(Color.WHITE);
            this.getContentPane().add(leastStepLabel, Integer.valueOf(1));

            JLabel lvLabel = new JLabel(String.format("Level: %d", this.lv));
            lvLabel.setFont(f);
            lvLabel.setLocation(new Point(this.gamePanel.getWidth() + 80, 20));
            lvLabel.setSize(180, 50);
            this.getContentPane().add(lvLabel, Integer.valueOf(1));
            lvLabel.setForeground(Color.WHITE);

            if (isMode()) {
                this.leftTimeLabel = new JLabel(String.format("Left time: %d", this.time));
                this.leftTimeLabel.setFont(f);
                this.leftTimeLabel.setLocation(new Point(this.gamePanel.getWidth() + 200, 20));
                this.leftTimeLabel.setSize(180, 50);
                this.getContentPane().add(this.leftTimeLabel, Integer.valueOf(1));
                this.leftTimeLabel.setForeground(Color.WHITE);
            } else {
                this.timeLabel = new JLabel(String.format("Time: %d", this.time));
                this.timeLabel.setFont(f);
                this.timeLabel.setLocation(new Point(this.gamePanel.getWidth() + 200, 20));
                this.timeLabel.setSize(180, 50);
                this.getContentPane().add(this.timeLabel, Integer.valueOf(1));
                this.timeLabel.setForeground(Color.WHITE);
            }

            JLabel stepLabel = new JLabel(String.format("Step: %d", step));
            stepLabel.setFont(f);
            stepLabel.setLocation(new Point(this.gamePanel.getWidth() + 80, 70));
            stepLabel.setSize(180, 50);
            this.getContentPane().add(stepLabel, Integer.valueOf(1));
            stepLabel.setForeground(Color.WHITE);
            this.gamePanel.setStepLabel(stepLabel);
        } else {
            loadBtn.setVisible(false);
            musicBtn.setVisible(false);
            this.sound.changeSource("resources/misc/东方永夜抄竹取飞翔.wav");
            this.sound.setVolume(0.5);
            this.sound.play();

            JLabel leastStepLabel = new JLabel("Min_Steps: ???");
            leastStepLabel.setFont(f);
            leastStepLabel.setLocation(new Point(this.gamePanel.getWidth() + 200, 70));
            leastStepLabel.setSize(180, 50);
            this.getContentPane().add(leastStepLabel, Integer.valueOf(1));
            leastStepLabel.setForeground(Color.WHITE);

            JLabel lvLabel = new JLabel("Level: ???");
            lvLabel.setFont(f);
            lvLabel.setLocation(new Point(this.gamePanel.getWidth() + 80, 20));
            lvLabel.setSize(180, 50);
            this.getContentPane().add(lvLabel, Integer.valueOf(1));
            lvLabel.setForeground(Color.WHITE);

            if (isMode()) {
                this.leftTimeLabel = new JLabel("Left time: ???");
                this.leftTimeLabel.setFont(f);
                this.leftTimeLabel.setLocation(new Point(this.gamePanel.getWidth() + 200, 20));
                this.leftTimeLabel.setSize(180, 50);
                this.getContentPane().add(this.leftTimeLabel, Integer.valueOf(1));
                this.leftTimeLabel.setForeground(Color.WHITE);
            } else {
                this.timeLabel = new JLabel("Time: ???");
                this.timeLabel.setFont(f);
                this.timeLabel.setLocation(new Point(this.gamePanel.getWidth() + 200, 20));
                this.timeLabel.setSize(180, 50);
                this.getContentPane().add(this.timeLabel, Integer.valueOf(1));
                this.timeLabel.setForeground(Color.WHITE);
            }

            JLabel stepLabel = new JLabel("Step: ???");
            stepLabel.setFont(f);
            stepLabel.setLocation(new Point(this.gamePanel.getWidth() + 80, 70));
            stepLabel.setSize(180, 50);
            this.getContentPane().add(stepLabel, Integer.valueOf(1));
            stepLabel.setForeground(Color.WHITE);
            this.gamePanel.setStepLabel(stepLabel);
        }

        restartBtn.addActionListener(_ -> {
            this.controller.restartGame();
            this.gamePanel.requestFocusInWindow();//enable key listener
        });

        loadBtn.addActionListener(_ -> {
            if (isMode() || this.lv == 6) {
                JOptionPane.showMessageDialog(this, "此模式无法存档喵~");
                this.gamePanel.requestFocusInWindow();
                return;
            }
            this.controller.getTimer().stop();
            if (this.user.getId() == 0) {
                JOptionPane.showMessageDialog(this, "游客模式不能存档喵~", "QAQ", JOptionPane.ERROR_MESSAGE);
                this.controller.getTimer().start();
                this.gamePanel.requestFocusInWindow();
            } else {
                if (this.check) {
                    Sound s = new Sound("resources/misc/zako.wav");
                    s.setVolume(0.8);
                    s.play();
                    JOptionPane.showOptionDialog(this, "不会要用存档才能过吧~ 雑魚♡~ 雑魚♡~", "雑魚♡~", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"我是杂鱼喵~", "私は雑魚にゃ♡~"}, "私は雑魚にゃ♡~");
                    this.check = false;
                }
                this.fileFrame.Show(this.fileFrame.getId());
                this.setVisible(false);
                this.fileFrame.setVisible(true);
                this.fileFrame.requestFocusInWindow();
            }
        });

        this.backBtn.addActionListener(_ -> {
            if (this.getLv() == 6) {
                this.sound.changeSource(this.musicPath);
                this.sound.setVolume(0.5);
                this.sound.play();
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
            if (this.gamePanel.getSteps() == 0) {
                JOptionPane.showMessageDialog(this, "步数为0，无法撤回喵", "Error", JOptionPane.INFORMATION_MESSAGE);
                this.gamePanel.requestFocusInWindow();
            } else {
                this.gamePanel.undoMove();
                this.gamePanel.requestFocusInWindow();
            }
        });

        musicBtn.addActionListener(_ -> {
            new MusicFrame(this, this.sound);
            this.gamePanel.requestFocusInWindow();
        });

        helpBtn.addActionListener(_ -> {
            JOptionPane.showMessageDialog(this, null, "Help", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(new ImageIcon("resources/images/Help.png").getImage().getScaledInstance(1200, 675, Image.SCALE_SMOOTH)));
            this.gamePanel.requestFocusInWindow();
        });

        upMoveBtn.addActionListener(_ -> {
            this.gamePanel.doMoveUp();
            this.gamePanel.requestFocusInWindow();//enable key listener
        });

        downMoveBtn.addActionListener(_ -> {
            this.gamePanel.doMoveDown();
            this.gamePanel.requestFocusInWindow();//enable key listener
        });

        leftMoveBtn.addActionListener(_ -> {
            this.gamePanel.doMoveLeft();
            this.gamePanel.requestFocusInWindow();//enable key listener
        });

        rightMoveBtn.addActionListener(_ -> {
            this.gamePanel.doMoveRight();
            this.gamePanel.requestFocusInWindow();//enable key listener
        });

        ImageIcon back = new ImageIcon("resources/images/Menu_Theme_The_Eternal_Ordeal.png");
        back.setImage(back.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        JLabel bg = new JLabel(back);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层

        if (!file.exists() && user.getId() != 0 && lv != 6) {
            if (!file.getParentFile().mkdirs()) {
                System.err.println("目录已存在喵: " + file.getParentFile().getAbsolutePath());
            }
            try {
                FileFrame.createFile(filepath);
                for (int i = 0; i < 6; i++) {
                    MapInfo mapInfo = new MapInfo();
                    mapInfo.setModel(null);
                    mapInfo.setId(i);
                    mapInfo.setStep(0);
                    FileFrame.addNewMap(mapInfo, filepath);
                }
                System.out.println("创建新文件并保存喵");
            } catch (Exception e) {
                System.err.println("保存失败喵");
                log.info(e.getMessage());
            }
            try {
                if (FileFrame.updateMapById(filepath, 0, this.controller.getModel(), this.gamePanel.getSteps(), this.gamePanel.getTime(), this.gamePanel.getMoveHero(), this.gamePanel.getMoveBox())) {
                    System.out.println("更新成功喵");
                } else {
                    System.err.println("更新失败喵");
                }
                FileSHAUtil.saveSHAToFile(FileSHAUtil.calculateSHA(new File(filepath)), new File(filepath + ".sha"));
            } catch (IOException e) {
                log.info(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        File shaFile = new File(filepath + ".sha");
        if (!shaFile.exists() && user.getId() != 0 && lv != 6) {
            System.err.println("存档文档损坏喵！");
            if (file.delete()) {
                System.err.println("存档已清空！！！");
                try {
                    FileFrame.createFile(filepath);
                    for (int i = 0; i < 6; i++) {
                        MapInfo mapInfo = new MapInfo();
                        mapInfo.setModel(null);
                        mapInfo.setId(i);
                        mapInfo.setStep(0);
                        FileFrame.addNewMap(mapInfo, filepath);
                    }
                    System.out.println("创建新文件并保存喵");
                } catch (Exception e) {
                    System.err.println("保存失败喵");
                    log.info(e.getMessage());
                }
                try {
                    if (FileFrame.updateMapById(filepath, 0, controller.getModel(), this.gamePanel.getSteps(), this.gamePanel.getTime(), this.gamePanel.getMoveHero(), this.gamePanel.getMoveBox())) {
                        System.out.println("更新成功喵");
                    } else {
                        System.out.println("更新失败喵");
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
        return this.backBtn;
    }

    public Sound getSound() {
        return this.sound;
    }

    public String getMusicPath() {
        return this.musicPath;
    }

    public JLabel getTimeLabel() {
        return this.timeLabel;
    }

    public LevelFrame getLevelFrame() {
        return this.levelFrame;
    }

    public User getUser() {
        return this.user;
    }

    public JLabel getLeftTimeLabel() {
        return this.leftTimeLabel;
    }

    public FileFrame getFileFrame() {
        return this.fileFrame;
    }

    public int getLv() {
        return this.lv;
    }

    public GameController getGameController() {
        return this.controller;
    }

    public GamePanel getGamePanel() {
        return this.gamePanel;
    }

    public GameController getController() {
        return this.controller;
    }

    public boolean isMode() {
        return this.mode;
    }

    public int getTime() {
        return this.time;
    }

    public int[] getLeastStep() {
        return this.leastStep;
    }
}
