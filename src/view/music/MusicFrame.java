package view.music;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Logger;

public class MusicFrame extends JFrame implements ActionListener {
    private final JFrame jFrame;
    private final JButton playBtn;
    private final JButton pauseBtn;
    private final JButton backBtn;
    private final String[] SongName;
    private final JSlider volumeSlider;  // 音量条
    private final JLabel statusLabel;  // 状态显示标签
    private int choose;

    public MusicFrame(JFrame jFrame, Sound sound) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            Logger log = Logger.getLogger(this.getClass().getName());
            log.info(e.getMessage());
        }
        this.jFrame = jFrame;
        this.jFrame.setVisible(false);
        choose = 1;
        SongName = new String[]{
                "東方紅魔郷魔法少女達の百年祭.wav",
                "東方紅魔郷亡き王女の为のセプテット.wav",
                "東方紅魔郷U.N.オーエンは彼女なのか？.wav",
                "东方永夜抄竹取飞翔.wav",
                "东方妖妖梦少女幻葬　～ Necro-Fantasy.wav",
                "Help me, ERINNNNNN!! feat.初音ミク (Game size) (feat. Hatsune Miku).wav",
                "Alphys.wav",
                "EnterHallownest.wav",
                "Main_Theme.wav",
                "恋ひ恋ふ縁.wav"
        };
        setLayout(null);
        setTitle("Music Player");
        setSize(300, 450);
        //设置界面一直处于最上层
//        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        this.setResizable(false);

        // 创建一个列表模型
        JScrollPane scrollPane = getJScrollPane(sound);
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane);

        this.pauseBtn = new JButton("⏸");
        Font f = new Font("", Font.PLAIN, 20);
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
                statusLabel.setText(String.format("Status: %s, Volume: %.0f%%",
                        sound.isPlaying() ? "Playing" : "Paused", sound.getVolume() * 100));
                sound.displayStatus();
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
                statusLabel.setText(String.format("Status: %s, Volume: %.0f%%",
                        sound.isPlaying() ? "Playing" : "Paused", sound.getVolume() * 100));
                sound.displayStatus();
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
        Font f2 = new Font("", Font.PLAIN, 20);
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

        statusLabel = new JLabel(String.format("Status: %s, Volume: %.0f%%",
                sound.isPlaying() ? "Playing" : "Paused", sound.getVolume() * 100));
        statusLabel.setBounds(10, 370, 200, 30);
        this.add(statusLabel);

        // 音量条
        volumeSlider = new JSlider(0, 100, 100);
        volumeSlider.setBounds(10, 370, 200, 10);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);

        this.add(volumeSlider);

        // 音量条拖动
        volumeSlider.addChangeListener(_ -> {
            double volume = volumeSlider.getValue() / 100.0;
            sound.setVolume(volume);
            statusLabel.setText(String.format("Status: %s, Volume: %.0f%%",
                    sound.isPlaying() ? "Playing" : "Paused", volume * 100));
        });

        this.setVisible(true);
    }

    private static JList<String> getSongList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("魔法少女達の百年祭");
        listModel.addElement("亡き王女の为のセプテット");
        listModel.addElement("U.N.オーエンは彼女なのか？");
        listModel.addElement("竹取飞翔");
        listModel.addElement("少女幻葬～Necro-Fantasy");
        listModel.addElement("Help me, ERINNNNNN!! feat.初音ミク");
        listModel.addElement("Alphys");
        listModel.addElement("EnterHallownest");
        listModel.addElement("Main_Theme");
        listModel.addElement("恋ひ恋ふ縁");
        // 创建列表，并设置选择监听器
        return new JList<>(listModel);
    }

    private JScrollPane getJScrollPane(Sound sound) {
        JList<String> songList = getSongList();
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // 处理列表项选择事件
                add(playBtn);
                remove(pauseBtn);
                sound.pause();
                sound.displayStatus();
                choose = songList.getSelectedIndex();
                String selectedSong = SongName[choose];
                sound.changeSource("src/misc/" + selectedSong);
                statusLabel.setText(String.format("Status: %s, Volume: %.0f%%",
                        sound.isPlaying() ? "Playing" : "Paused", sound.getVolume() * 100));
                revalidate();
                repaint();
            }
        });
        songList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    add(pauseBtn);
                    remove(playBtn);
                    sound.play();
                    statusLabel.setText(String.format("Status: %s, Volume: %.0f%%",
                            sound.isPlaying() ? "Playing" : "Paused", sound.getVolume() * 100));
                    sound.displayStatus();
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
        return scrollPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            this.setVisible(false);
            jFrame.setVisible(true);
        }
    }
}
