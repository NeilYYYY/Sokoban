package view.game;

import javax.swing.*;
import java.awt.*;

import controller.GameController;
import model.MapMatrix;
import model.Sound;
import view.FrameUtil;
import view.level.LevelFrame;
import view.login.LoginFrame;
import view.login.User;

public class GameFrame extends JFrame {

    private final GameController controller;
    private final JButton restartBtn;
    private final JButton loadBtn;
    private final JButton backBtn;
    private final JLabel stepLabel;
    private final GamePanel gamePanel;
    private final JLabel lvLabel;
    private final JButton playSoundBtn;
    private final JButton stopSoundBtn;
    private final JButton upMoveBtn;
    private final JButton downMoveBtn;
    private final JButton leftMoveBtn;
    private final JButton rightMoveBtn;
    private final Sound sound;
    private final int lv;
    private User user;

    public GameFrame(int width, int height, MapMatrix mapMatrix, User user, int lv) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Font font = new Font("Arial", Font.BOLD, 25);
        this.sound = new Sound("src/misc/EnterHallownest.wav");
        this.sound.start(true);
        this.lv = lv;
        this.setTitle(String.format("Level %d", this.lv));
        this.setLayout(null);
        this.setSize(width, height);
        gamePanel = new GamePanel(mapMatrix, this, user);
        gamePanel.setFocusable(true);
        gamePanel.setLocation(30, height / 2 - gamePanel.getHeight() / 2);
        this.add(gamePanel);
        this.controller = new GameController(gamePanel, mapMatrix, user, lv);
        System.out.println(user);
        this.restartBtn = FrameUtil.createButton(this, "Restart", new Point(gamePanel.getWidth() + 80, 120), 80, 50);
        this.loadBtn = FrameUtil.createButton(this, "Savings", new Point(gamePanel.getWidth() + 80, 180), 80, 50);
        this.backBtn = FrameUtil.createButton(this, "Back", new Point(gamePanel.getWidth() + 80, 240), 80, 50);
        this.playSoundBtn = FrameUtil.createButton(this, "Play Music", new Point(gamePanel.getWidth() + 180, 120), 100, 50);
        this.stopSoundBtn = FrameUtil.createButton(this, "Stop Music", new Point(gamePanel.getWidth() + 180, 180), 100, 50);
        this.upMoveBtn = FrameUtil.createButton(this, "↑", new Point(gamePanel.getWidth() + 220, 260), 30, 30);
        upMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        upMoveBtn.setBorderPainted(false);
        upMoveBtn.setBorder(null);
        upMoveBtn.setFocusPainted(false);
        upMoveBtn.setContentAreaFilled(false);
        upMoveBtn.setFont(font);
        this.downMoveBtn = FrameUtil.createButton(this, "↓", new Point(gamePanel.getWidth() + 220, 320), 30, 30);
        downMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        downMoveBtn.setBorderPainted(false);
        downMoveBtn.setBorder(null);
        downMoveBtn.setFocusPainted(false);
        downMoveBtn.setContentAreaFilled(false);
        downMoveBtn.setFont(font);
        this.leftMoveBtn = FrameUtil.createButton(this, "←", new Point(gamePanel.getWidth() + 190, 290), 30, 30);
        leftMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        leftMoveBtn.setBorderPainted(false);
        leftMoveBtn.setBorder(null);
        leftMoveBtn.setFocusPainted(false);
        leftMoveBtn.setContentAreaFilled(false);
        leftMoveBtn.setFont(font);
        this.rightMoveBtn = FrameUtil.createButton(this, "→", new Point(gamePanel.getWidth() + 250, 290), 30, 30);
        rightMoveBtn.setMargin(new Insets(0, 0, 0, 0));
        rightMoveBtn.setBorderPainted(false);
        rightMoveBtn.setBorder(null);
        rightMoveBtn.setFocusPainted(false);
        rightMoveBtn.setContentAreaFilled(false);
        rightMoveBtn.setFont(font);
        this.stepLabel = FrameUtil.createJLabel(this, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 70), 180, 50);
        gamePanel.setStepLabel(stepLabel);
        this.lvLabel = FrameUtil.createJLabel(this, String.format("Level: %d", this.lv), new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 20), 180, 50);
        this.restartBtn.addActionListener(_ -> {
            controller.restartGame();
            gamePanel.requestFocusInWindow();//enable key listener
            this.sound.stop();
        });
        this.loadBtn.addActionListener(_ -> {
            FileFrame fileFrame = new FileFrame(1000, 1000, user, this, this.lv);
            fileFrame.setVisible(false);
            this.dispose();
            fileFrame.setVisible(true);
            this.sound.stop();
            gamePanel.requestFocusInWindow();
        });
        this.backBtn.addActionListener(_ -> {
            LevelFrame levelFrame = new LevelFrame(user);
            this.dispose();
            levelFrame.setVisible(true);
            levelFrame.getSound().start(true);
            this.sound.stop();
        });
        this.playSoundBtn.addActionListener(_ -> {
            this.sound.continues();
            gamePanel.requestFocusInWindow();
        });
        this.stopSoundBtn.addActionListener(_ -> {
            this.sound.stop();
            gamePanel.requestFocusInWindow();
        });
        this.upMoveBtn.addActionListener(_ -> {
            gamePanel.doMoveUp();
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.downMoveBtn.addActionListener(_ -> {
            gamePanel.doMoveDown();
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.leftMoveBtn.addActionListener(_ -> {
            gamePanel.doMoveLeft();
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.rightMoveBtn.addActionListener(_ -> {
            gamePanel.doMoveRight();
            gamePanel.requestFocusInWindow();//enable key listener
        });
        //todo: add other button here
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public GameController getGameController() {
        return controller;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public Sound getSound() {
        return sound;
    }

    public GameController getController() {
        return controller;
    }
}
