package view.music;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MusicFrame extends JFrame implements ActionListener {
    private final JFrame jFrame;
    private final JButton playBtn;
    private final JButton pauseBtn;
    private final JButton backBtn;
    private final String[] SongName;
    private final JSlider volumeSlider;
    private final JLabel statusLabel;
    private final Sound sound;
    private int choose;
    private final JProgressBar progressBar;
    private final JList<String> songList = getSongList();

    public MusicFrame(JFrame jFrame, Sound sound) {
        this.jFrame = jFrame;
        this.jFrame.setVisible(false);
        this.sound = sound;
        setLayout(null);
        setTitle("Music Player");
        setSize(300, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        this.setResizable(false);
        Font f = new Font("", Font.PLAIN, 20);
        Font f2 = new Font("", Font.PLAIN, 20);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.setContentPane(layeredPane);

        JScrollPane scrollPane = getJScrollPane(this.sound);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        scrollPane.setBorder(null);
        this.getContentPane().add(scrollPane, Integer.valueOf(0));

        this.choose = Sound.getIndex();
        this.SongName = new String[]{
                "東方紅魔郷魔法少女達の百年祭.wav",
                "東方紅魔郷亡き王女の为のセプテット.wav",
                "東方紅魔郷U.N.オーエンは彼女なのか？.wav",
                "东方永夜抄竹取飞翔.wav",
                "东方妖妖梦少女幻葬　～ Necro-Fantasy.wav",
                "Help me, ERINNNNNN!! feat.初音ミク (Game size) (feat. Hatsune Miku).wav",
                "Alphys.wav",
                "EnterHallownest.wav",
                "Breath_of_Wild_Main_Theme.wav",
                "恋ひ恋ふ縁.wav"
        };

        this.pauseBtn = new JButton("⏸");
        this.pauseBtn.setFont(f);
        this.pauseBtn.setForeground(Color.WHITE);
        this.pauseBtn.setToolTipText("Pause");
        this.pauseBtn.setBounds(130, 300, 30, 30);
        this.pauseBtn.setBorder(BorderFactory.createEmptyBorder());
        this.pauseBtn.setFocusPainted(false);
        this.pauseBtn.setMargin(new Insets(0, 0, 0, 0));
        this.pauseBtn.setBorderPainted(false);
        this.pauseBtn.setBorder(null);
        this.pauseBtn.setFocusPainted(false);
        this.pauseBtn.setContentAreaFilled(false);
        this.pauseBtn.addActionListener(this);
        this.pauseBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                pauseBtn.setForeground(new Color(255, 255, 0)); // 鼠标进入时，变为红色
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pauseBtn.setForeground(Color.WHITE); // 鼠标离开时，恢复为白色
            }
        });
        if (this.sound.isPlaying()) {
            this.getContentPane().add(this.pauseBtn, Integer.valueOf(0));
        }

        this.playBtn = new JButton("▶");
        this.playBtn.setFont(f);
        this.playBtn.setForeground(Color.WHITE);
        this.playBtn.setToolTipText("Play");
        this.playBtn.setBounds(130, 300, 30, 30);
        this.playBtn.setBorder(BorderFactory.createEmptyBorder());
        this.playBtn.setFocusPainted(false);
        this.playBtn.setMargin(new Insets(0, 0, 0, 0));
        this.playBtn.setBorderPainted(false);
        this.playBtn.setBorder(null);
        this.playBtn.setFocusPainted(false);
        this.playBtn.setContentAreaFilled(false);
        this.playBtn.addActionListener(this);
        this.playBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                playBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                playBtn.setForeground(Color.WHITE);
            }
        });
        if (!this.sound.isPlaying()) {
            this.getContentPane().add(this.playBtn, Integer.valueOf(0));
        }

        this.backBtn = new JButton("⮐");
        this.backBtn.setFont(f2);
        this.backBtn.setForeground(Color.WHITE);
        this.backBtn.setToolTipText("Back");
        this.backBtn.setBounds(10, 10, 30, 30);
        this.backBtn.setBorder(BorderFactory.createEmptyBorder());
        this.backBtn.setFocusPainted(false);
        this.backBtn.setMargin(new Insets(0, 0, 0, 0));
        this.backBtn.setBorderPainted(false);
        this.backBtn.setBorder(null);
        this.backBtn.setFocusPainted(false);
        this.backBtn.setContentAreaFilled(false);
        this.backBtn.addActionListener(this);
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
        this.getContentPane().add(this.backBtn, Integer.valueOf(0));

        this.statusLabel = new JLabel(String.format("Status: %s, Volume: %.0f%%", this.sound.isPlaying() ? "Playing" : "Paused", this.sound.getVolume() * 100));
        this.statusLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        this.statusLabel.setForeground(Color.WHITE);
        this.statusLabel.setBounds(10, 370, 200, 30);
        this.getContentPane().add(this.statusLabel, Integer.valueOf(0));

        this.progressBar = new JProgressBar();
        this.progressBar.setBounds(10, 340, 270, 15);
        progressBar.setStringPainted(true); // 显示文本
        this.getContentPane().add(progressBar, Integer.valueOf(0));

        Timer progressTimer = new Timer(500, _ -> {
            long currentFrame = sound.getCurrentFrame();
            long totalFrames = sound.getClipLength();

            if (totalFrames > 0) {
                int progress = (int) (100 * currentFrame / totalFrames);
                progressBar.setValue(progress); // 更新进度
                float frameRate = sound.getFrameRate();
                // 计算已播放时间和总时长
                if (frameRate > 0) {
                    long currentSeconds = (long) (currentFrame / frameRate);
                    long totalSeconds = (long) (totalFrames / frameRate);
                    // 转换为分钟和秒
                    String currentTime = String.format("%02d:%02d", currentSeconds / 60, currentSeconds % 60);
                    String totalTime = String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
                    // 设置显示文本
                    progressBar.setString(currentTime + "/" + totalTime);
                }
            }
        });
        progressTimer.start();

        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                playBtn.doClick();
                try {
                    updateProgress(e);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    updateProgress(e);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                requestFocusInWindow();
            }

            private void updateProgress(@NotNull MouseEvent e) throws InterruptedException {
                int mouseX = e.getX();
                int progressBarVal = (int) Math.round(((double) mouseX / (double) progressBar.getWidth()) * 100);
                progressBar.setValue(progressBarVal);

                long totalFrames = sound.getClipLength();
                long newFrame = (totalFrames * progressBarVal) / 100;
                sound.setCurrentFrame(newFrame);
            }
        });

        this.volumeSlider = new JSlider(0, 100, (int) (this.sound.getVolume() * 100));
        this.volumeSlider.setBounds(10, 370, 200, 10);
        this.volumeSlider.setPaintTicks(true);
        this.volumeSlider.setPaintLabels(true);
        this.volumeSlider.setOpaque(false);
        this.volumeSlider.setFocusable(false);
        this.getContentPane().add(this.volumeSlider, Integer.valueOf(0));

        this.volumeSlider.addChangeListener(_ -> {
            double volume = this.volumeSlider.getValue() / 100.0;
            this.sound.setVolume(volume);
            this.statusLabel.setText(String.format("Status: %s, Volume: %.0f%%", this.sound.isPlaying() ? "Playing" : "Paused", volume * 100));
            this.requestFocusInWindow();
        });

        ImageIcon back = new ImageIcon("resources/images/MusicFrameBackground.png");
        back.setImage(back.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
        JLabel bg = new JLabel(back);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1));

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE -> {
                        if (sound.isPlaying()) {
                            pauseBtn.doClick();
                        } else {
                            playBtn.doClick();
                        }
                    }
                    case KeyEvent.VK_UP -> {
                        int previousIndex = choose - 1;
                        if (previousIndex >= 0) {
                            choose = previousIndex;
                            songList.setSelectedIndex(choose);
                        }
                    }
                    case KeyEvent.VK_DOWN -> {
                        int nextIndex = choose + 1;
                        if (nextIndex < SongName.length) {
                            choose = nextIndex;
                            songList.setSelectedIndex(choose);
                        }
                    }
                    case KeyEvent.VK_LEFT -> {
                        playBtn.doClick();
                        long currentFrame = sound.getCurrentFrame();
                        long newFrame = currentFrame - (long) (sound.getFrameRate() * 5); // Rewind 5 seconds
                        if (newFrame < 0) newFrame = 0;
                        try {
                            sound.setCurrentFrame(newFrame);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        playBtn.doClick();
                        long currentFrame = sound.getCurrentFrame();
                        long newFrame = currentFrame + (long) (sound.getFrameRate() * 5); // Fast-forward 5 seconds
                        if (newFrame > sound.getClipLength()) newFrame = sound.getClipLength();
                        try {
                            sound.setCurrentFrame(newFrame);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    case KeyEvent.VK_ESCAPE -> backBtn.doClick();
                }
            }
        });
        SwingUtilities.invokeLater(this::requestFocusInWindow);
        this.setVisible(true);
    }

    @Contract(" -> new")
    private static @NotNull JList<String> getSongList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("魔法少女達の百年祭");
        listModel.addElement("亡き王女の为のセプテット");
        listModel.addElement("U.N.オーエンは彼女なのか？");
        listModel.addElement("竹取飞翔");
        listModel.addElement("少女幻葬～Necro-Fantasy");
        listModel.addElement("Help me, ERINNNNNN!! feat.初音ミク");
        listModel.addElement("Alphys");
        listModel.addElement("EnterHallownest");
        listModel.addElement("旷野之息 Main Theme");
        listModel.addElement("恋ひ恋ふ縁");
        return new JList<>(listModel);
    }

    private @NotNull JScrollPane getJScrollPane(Sound sound) {
        this.songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.songList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                this.choose = this.songList.getSelectedIndex();
                String selectedSong = this.SongName[this.choose];
                if (this.choose != Sound.getIndex()) {
                    if (this.sound.isPlaying()) {
                        this.getContentPane().remove(this.pauseBtn);
                        this.getContentPane().add(this.playBtn, Integer.valueOf(0));
                        this.sound.pause();
                    }
                    this.sound.changeSource("resources/misc/" + selectedSong);
                    Sound.setIndex(choose);
                    this.sound.setVolume(0.5);
                    this.statusLabel.setText(String.format("Status: %s, Volume: %.0f%%", this.sound.isPlaying() ? "Playing" : "Paused", this.sound.getVolume() * 100));
                }
                this.requestFocusInWindow();
                revalidate();
                repaint();
            }
        });
        this.songList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!sound.isPlaying()) {
                        remove(playBtn);
                        add(pauseBtn, Integer.valueOf(0));
                        pauseBtn.requestFocusInWindow();
                        sound.play();
                    }
                    statusLabel.setText(String.format("Status: %s, Volume: %.0f%%", sound.isPlaying() ? "Playing" : "Paused", sound.getVolume() * 100));
                    volumeSlider.setValue(50);
                    requestFocusInWindow();
                    revalidate();
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        this.songList.setCellRenderer((_, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value);
            label.setFont(new Font("Serif", Font.BOLD, 12));
            if (isSelected || cellHasFocus || index == Sound.getIndex()) {
                label.setOpaque(false);
                label.setForeground(Color.YELLOW);
            } else {
                label.setOpaque(false);
                label.setForeground(Color.WHITE);
            }
            return label;
        });
        this.songList.setOpaque(false);

        // 将列表放置在滚动面板中，并将滚动面板添加到悬浮窗口中
        JScrollPane scrollPane = new JScrollPane(this.songList);
        scrollPane.setBounds(35, 35, 220, 260);
        return scrollPane;
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        if (e.getSource() == this.backBtn) {
            this.setVisible(false);
            this.jFrame.setVisible(true);
        } else if (e.getSource() == this.playBtn) {
            if (this.sound.isPlaying()) {
                return;
            }
            this.getContentPane().remove(this.playBtn);
            this.getContentPane().add(this.pauseBtn, Integer.valueOf(0));
            this.requestFocusInWindow();
            this.sound.play();
            this.statusLabel.setText(String.format("Status: %s, Volume: %.0f%%", this.sound.isPlaying() ? "Playing" : "Paused", this.sound.getVolume() * 100));
            this.revalidate();
            this.repaint();
        } else if (e.getSource() == this.pauseBtn) {
            if (!this.sound.isPlaying()) {
                return;
            }
            this.getContentPane().remove(this.pauseBtn);
            this.getContentPane().add(this.playBtn, Integer.valueOf(0));
            this.requestFocusInWindow();
            this.sound.pause();
            this.statusLabel.setText(String.format("Status: %s, Volume: %.0f%%", this.sound.isPlaying() ? "Playing" : "Paused", this.sound.getVolume() * 100));
            this.revalidate();
            this.repaint();
        }
    }
}
