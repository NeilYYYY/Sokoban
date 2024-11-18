package view.music;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

public class MusicFrame extends JFrame implements ActionListener {
    private final JFrame jFrame;
    private final Font f = new Font("", Font.PLAIN, 20);
    private final Font f2 = new Font("", Font.PLAIN, 20);
    private final JButton playBtn;
    private final JButton pauseBtn;
    private final JButton backBtn;
    private final Sound sound;
    private final String[] SongName;
    private int choose;
    private JSlider progressSlider;  // 进度条
    private JSlider volumeSlider;  // 音量条
    private JLabel statusLabel;  // 状态显示标签

    public MusicFrame(JFrame jFrame, Sound sound) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.jFrame = jFrame;
        this.jFrame.setVisible(false);
        choose = 1;
        SongName = new String[]{
                "東方紅魔郷魔法少女達の百年祭.mid",
                "東方紅魔郷亡き王女の为のセプテット.mid",
                "東方紅魔郷U.N.オーエンは彼女なのか？.mid",
                "东方永夜抄竹取飞翔.mid", "Help me, ERINNNNNN!! feat.初音ミク (Game size) (feat. Hatsune Miku).wav",
                "Alphys.wav",
                "EnterHallownest.wav",
                "Main_Theme.wav",
                "恋ひ恋ふ縁.wav"
        };
        setLayout(null);
        setTitle("Music Player");
        setSize(300, 450);
        setAlwaysOnTop(true);//设置界面一直处于最上层
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        this.setResizable(false);

        // 创建一个列表模型
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("魔法少女達の百年祭");
        listModel.addElement("亡き王女の为のセプテット");
        listModel.addElement("U.N.オーエンは彼女なのか？");
        listModel.addElement("竹取飞翔");
        listModel.addElement("Help me, ERINNNNNN!! feat.初音ミク");
        listModel.addElement("Alphys");
        listModel.addElement("EnterHallownest");
        listModel.addElement("Main_Theme");
        listModel.addElement("恋ひ恋ふ縁");
        // 创建列表，并设置选择监听器
        JList<String> songList = new JList<>(listModel);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // 处理列表项选择事件
                    add(playBtn);
                    remove(pauseBtn);
                    sound.pause();
                    revalidate();
                    repaint();
                }
            }
        });
        songList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    add(pauseBtn);
                    remove(playBtn);
                    choose = songList.getSelectedIndex();
                    String selectedSong = SongName[choose];
                    sound.changeSource("src/misc/" + selectedSong);
                    sound.play();
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
        });//增加双击功能
        songList.setOpaque(false);

        // 将列表放置在滚动面板中，并将滚动面板添加到悬浮窗口中
        JScrollPane scrollPane = new JScrollPane(songList);
        scrollPane.setBounds(35, 35, 220, 260);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane);

        this.sound = sound;
        this.pauseBtn = new JButton("⏸");
        this.pauseBtn.setFont(f);
        this.pauseBtn.setToolTipText("Pause");
        this.pauseBtn.setBounds(130, 300, 30, 30);
        this.pauseBtn.setBorder(BorderFactory.createEmptyBorder());
        this.pauseBtn.setFocusPainted(false);
        this.pauseBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // 鼠标进入按钮时的效果
                pauseBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 鼠标离开按钮时的效果恢复到默认状态
                pauseBtn.setBorder(BorderFactory.createEmptyBorder());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                pauseBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                remove(pauseBtn);
                add(playBtn);
                sound.pause();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                pauseBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
        });
        if (sound.isPlaying()) {
            this.add(this.pauseBtn);
        }

        this.playBtn = new JButton("▶");
        this.playBtn.setFont(f);
        this.playBtn.setToolTipText("Play");
        this.playBtn.setBounds(130, 300, 30, 30);
        this.playBtn.setBorder(BorderFactory.createEmptyBorder());
        this.playBtn.setFocusPainted(false);
        this.playBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // 鼠标进入按钮时的效果
                playBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 鼠标离开按钮时的效果恢复到默认状态
                playBtn.setBorder(BorderFactory.createEmptyBorder());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                playBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                remove(playBtn);
                add(pauseBtn);
                sound.play();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                playBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
        });
        if (!sound.isPlaying()) {
            this.add(this.playBtn);
        }

        this.backBtn = new JButton("⮐");
        this.backBtn.setFont(f2);
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
        this.add(this.backBtn);

        statusLabel = new JLabel("Volume: 100%");
        statusLabel.setBounds(10, 370, 200, 30);
        this.add(statusLabel);

        // 进度条
        progressSlider = new JSlider(0, 100, 0);
        progressSlider.setBounds(10, 350, 200, 10);
        progressSlider.setPaintTicks(true);
        progressSlider.setPaintLabels(true);

        // 音量条
        volumeSlider = new JSlider(0, 100, 100);
        volumeSlider.setBounds(10, 370, 200, 10);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);

        this.add(progressSlider);
        this.add(volumeSlider);

        // 进度条拖动
        progressSlider.addChangeListener(_ -> {
            if (!progressSlider.getValueIsAdjusting() && !sound.isPlaying()) {
                double progress = progressSlider.getValue() / 100.0;
                sound.setProgress((long) (progress * sound.getDuration() * sound.audioFormat.getFrameRate()));
            }
        });

        // 音量条拖动
        volumeSlider.addChangeListener(_ -> {
            double volume = volumeSlider.getValue() / 100.0;
            sound.setVolume(volume);
            statusLabel.setText(String.format("Status: %s, Volume: %.0f%%",
                    sound.isPlaying() ? "Playing" : "Paused", volume * 100));
        });


        // 启动更新进度条的线程
        startProgressUpdater();

        this.setVisible(true);
    }

    // 启动线程更新进度条
    private void startProgressUpdater() {
        new Thread(() -> {
            while (true) {
                if (sound.isPlaying()) {
                    double progress = sound.getProgress();
                    progressSlider.setValue((int) progress);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            this.setVisible(false);
            jFrame.setVisible(true);
        }
    }
}
