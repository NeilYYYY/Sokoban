package view.game;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import controller.GameController;
import model.MapMatrix;
import view.FileMD5Util;
import view.FrameUtil;
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
    private boolean check = true;

    public GameFrame(int width, int height, MapMatrix mapMatrix, User user, int lv, int step, Sound sound) {
        Logger log = Logger.getLogger(GameFrame.class.getName());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        Font font = new Font("Arial", Font.BOLD, 25);
        this.lv = lv;
        this.setTitle(String.format("Level %d", this.lv));
        this.setLayout(null);
        this.setSize(width, height);
        this.user = user;
        this.sound = sound;
        this.setResizable(false);
        String filepath = String.format("src/saves/%d-%d.json", this.lv, user.id());
        File file = new File(filepath);
        gamePanel = new GamePanel(mapMatrix, this, this.user, step);
        gamePanel.setFocusable(true);
        gamePanel.setLocation(30, height / 2 - gamePanel.getHeight() / 2);
        this.add(gamePanel);
        SwingUtilities.invokeLater(gamePanel::requestFocusInWindow);
        this.controller = new GameController(gamePanel, mapMatrix, this.user, this.lv, this.sound);
        System.out.println(this.user);
        this.fileFrame = new FileFrame(800, 450, this.user, this, this.lv, this.sound);
        JButton restartBtn = FrameUtil.createButton(this, "Restart", new Point(gamePanel.getWidth() + 80, 120), 80, 50);
        JButton loadBtn = FrameUtil.createButton(this, "Savings", new Point(gamePanel.getWidth() + 80, 180), 80, 50);
        JButton backBtn = FrameUtil.createButton(this, "Back", new Point(gamePanel.getWidth() + 80, 240), 80, 50);
        JButton musicBtn = FrameUtil.createButton(this, "Music", new Point(gamePanel.getWidth() + 180, 120), 80, 50);
        JButton undoBtn = FrameUtil.createButton(this, "Undo", new Point(gamePanel.getWidth() + 180, 180), 80, 50);
        JButton upMoveBtn = FrameUtil.createButton(this, "↑", new Point(gamePanel.getWidth() + 220, 260), 30, 30);
        upMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        upMoveBtn.setBorderPainted(false);
        upMoveBtn.setBorder(null);
        upMoveBtn.setFocusPainted(false);
        upMoveBtn.setContentAreaFilled(false);
        upMoveBtn.setFont(font);
        upMoveBtn.setForeground(Color.WHITE);
        JButton downMoveBtn = FrameUtil.createButton(this, "↓", new Point(gamePanel.getWidth() + 220, 320), 30, 30);
        downMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        downMoveBtn.setBorderPainted(false);
        downMoveBtn.setBorder(null);
        downMoveBtn.setFocusPainted(false);
        downMoveBtn.setContentAreaFilled(false);
        downMoveBtn.setFont(font);
        downMoveBtn.setForeground(Color.WHITE);
        JButton leftMoveBtn = FrameUtil.createButton(this, "←", new Point(gamePanel.getWidth() + 190, 290), 30, 30);
        leftMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        leftMoveBtn.setBorderPainted(false);
        leftMoveBtn.setBorder(null);
        leftMoveBtn.setFocusPainted(false);
        leftMoveBtn.setContentAreaFilled(false);
        leftMoveBtn.setFont(font);
        leftMoveBtn.setForeground(Color.WHITE);
        JButton rightMoveBtn = FrameUtil.createButton(this, "→", new Point(gamePanel.getWidth() + 250, 290), 30, 30);
        rightMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        rightMoveBtn.setBorderPainted(false);
        rightMoveBtn.setBorder(null);
        rightMoveBtn.setFocusPainted(false);
        rightMoveBtn.setContentAreaFilled(false);
        rightMoveBtn.setFont(font);
        rightMoveBtn.setForeground(Color.WHITE);
        Font f = new Font("Comic Sans MS", Font.PLAIN, 22);
        JLabel stepLabel = FrameUtil.createJLabel(this, String.format("Step: %d", step), f, new Point(gamePanel.getWidth() + 80, 70), 180, 50);
        stepLabel.setForeground(Color.WHITE);
        gamePanel.setStepLabel(stepLabel);
        int[] leastStep = {13, 23, 31, 27, 37};
        JLabel leastStepLabel = FrameUtil.createJLabel(this, String.format("Min_Steps: %d", leastStep[lv - 1]), f, new Point(gamePanel.getWidth() + 200, 70), 180, 50);
        leastStepLabel.setForeground(Color.WHITE);
        JLabel lvLabel = FrameUtil.createJLabel(this, String.format("Level: %d", this.lv), f, new Point(gamePanel.getWidth() + 80, 20), 180, 50);
        lvLabel.setForeground(Color.WHITE);
        restartBtn.addActionListener(_ -> {
            controller.restartGame();
            gamePanel.requestFocusInWindow();//enable key listener
        });
        loadBtn.addActionListener(_ -> {
//            if (this.user.getId() == 0) {
//                JOptionPane.showMessageDialog(this, "游客模式不能存档喵~", "QAQ", JOptionPane.ERROR_MESSAGE);
//            } else {
            if (check){
                JOptionPane.showOptionDialog(this, "不会要用存档才能过吧~ 雑魚♡~ 雑魚♡~", "雌小鬼语录", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[] {"我是杂鱼喵~", "私は雑魚にゃ♡~"}, "私は雑魚にゃ♡~");
                check = false;
            }
            fileFrame.Show(0);
            this.setVisible(false);
            fileFrame.setVisible(true);
            gamePanel.requestFocusInWindow();
//            }
            //todo 这里是游客模式功能限制 记得去掉注释！！！！！！！
        });
        backBtn.addActionListener(_ -> {
            LevelFrame levelFrame = new LevelFrame(this.user, this.sound);
            this.dispose();
            levelFrame.setVisible(true);
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
        this.add(bg);
        if (!file.exists()) {
            MapInfo mapInfo = new MapInfo();
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
                System.out.println("保存失败");
                log.info(e.getMessage());
            }
            try {
                boolean result = FileFrame.updateMapById(0, controller.getModel(), this.gamePanel.getSteps(), this.gamePanel.getMoveHero(), this.gamePanel.getMoveBox(), filepath);
                if (result) {
                    System.out.println("更新成功");
                } else {
                    System.out.println("更新失败");
                }
                FileMD5Util.saveMD5ToFile(FileMD5Util.calculateMD5(new File(filepath)), new File(filepath + ".md5"));
            } catch (IOException e) {
                log.info(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        File md5File = new File(filepath + ".md5");
        if (!md5File.exists()) {
            System.out.println("存档文档损坏喵！");
            if (file.delete()) {
                System.out.println("存档已清空！！！");
                MapInfo mapInfo = new MapInfo();
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
                    System.out.println("保存失败");
                    log.info(e.getMessage());
                }
                try {
                    boolean result = FileFrame.updateMapById(0, controller.getModel(), this.gamePanel.getSteps(), this.gamePanel.getMoveHero(), this.gamePanel.getMoveBox(), filepath);
                    if (result) {
                        System.out.println("更新成功");
                    } else {
                        System.out.println("更新失败");
                    }
                    FileMD5Util.saveMD5ToFile(FileMD5Util.calculateMD5(new File(filepath)), new File(filepath + ".md5"));
                } catch (IOException e) {
                    log.info(e.getMessage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
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
}
