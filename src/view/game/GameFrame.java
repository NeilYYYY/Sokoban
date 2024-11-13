package view.game;

import javax.swing.*;
import java.awt.*;

import controller.GameController;
import model.MapMatrix;
import model.Sound;
import view.FrameUtil;
import view.level.LevelFrame;
import view.login.User;

public class GameFrame extends JFrame {

    private final GameController controller;
    private final JButton restartBtn;
    private final JButton loadBtn;
    private final JButton backBtn;
    private final JLabel stepLabel;
    private final GamePanel gamePanel;
    private final JLabel lvLabel;
    public Sound sound;
    private JButton playSoundBtn;
    private JButton stopSoundBtn;
    private User user;
    private int lv;

    public GameFrame(int width, int height, MapMatrix mapMatrix, User user, int lv) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.sound = new Sound("src\\misc\\EnterHallownest.wav");
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
        this.loadBtn = FrameUtil.createButton(this, "Load", new Point(gamePanel.getWidth() + 80, 210), 80, 50);
        this.backBtn = FrameUtil.createButton(this, "Back", new Point(gamePanel.getWidth() + 80, 300), 80, 50);
        this.playSoundBtn = FrameUtil.createButton(this, "Play Music", new Point(gamePanel.getWidth() + 180, 120), 100, 50);
        this.stopSoundBtn = FrameUtil.createButton(this, "Stop Music", new Point(gamePanel.getWidth() + 180, 210), 100, 50);
        this.stepLabel = FrameUtil.createJLabel(this, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 70), 180, 50);
        gamePanel.setStepLabel(stepLabel);
        this.lvLabel = FrameUtil.createJLabel(this, String.format("Level: %d", this.lv), new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() + 80, 20), 180, 50);
        this.restartBtn.addActionListener(_ -> {
            controller.restartGame();
            gamePanel.requestFocusInWindow();//enable key listener
            sound.stop();
        });
        this.loadBtn.addActionListener(_ -> {
            String string = JOptionPane.showInputDialog(this, "Input path:");
            System.out.println(string);
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.backBtn.addActionListener(_ -> {
            LevelFrame levelFrame = new LevelFrame(510, 200, user);
            levelFrame.setVisible(false);
            this.dispose();
            levelFrame.setVisible(true);
            this.setVisible(false);
            this.sound.stop();
        });
        this.sound.start(true);
        this.playSoundBtn.addActionListener(_ -> {
            sound.continues();
            gamePanel.requestFocusInWindow();
        });
        this.stopSoundBtn.addActionListener(_ -> {
            sound.stop();
            gamePanel.requestFocusInWindow();
        });
        //todo: add other button here
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
