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
    public Sound sound;
    private User user;
    private int lv;

    public GameFrame(int width, int height, MapMatrix mapMatrix, User user, int lv) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.sound = new Sound("src/misc/EnterHallownest.wav");
//        this.sound.start(true);
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
        this.upMoveBtn = FrameUtil.createButton(this, "Up", new Point(gamePanel.getWidth() + 220, 250), 70, 70);
        this.downMoveBtn = FrameUtil.createButton(this, "Down", new Point(gamePanel.getWidth() + 220, 330), 70, 70);
        this.leftMoveBtn = FrameUtil.createButton(this, "Left", new Point(gamePanel.getWidth() + 140, 330), 70, 70);
        this.rightMoveBtn = FrameUtil.createButton(this, "Right", new Point(gamePanel.getWidth() + 300, 330), 70, 70);
        this.stepLabel = FrameUtil.createJLabel(this, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 70), 180, 50);
        gamePanel.setStepLabel(stepLabel);
        this.lvLabel = FrameUtil.createJLabel(this, String.format("Level: %d", this.lv), new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 20), 180, 50);
        this.restartBtn.addActionListener(_ -> {
            controller.restartGame();
            gamePanel.requestFocusInWindow();//enable key listener
            this.sound.stop();
        });
        this.loadBtn.addActionListener(_ -> {
            FileFrame fileFrame = new FileFrame(510, 200, user, this, this.lv);
            fileFrame.setVisible(true);
            this.sound.stop();
            gamePanel.requestFocusInWindow();
        });
        this.backBtn.addActionListener(_ -> {
            LevelFrame levelFrame = new LevelFrame(user);
            levelFrame.setVisible(false);
            this.dispose();
            levelFrame.setVisible(true);
            this.setVisible(false);
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
}
