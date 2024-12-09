package view.music;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
    private final JSlider volumeSlider;
    private final JLabel statusLabel;
    private final Sound sound;
    private int choose;

    public MusicFrame(JFrame jFrame, Sound sound) {
        try {
            String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            Logger log = Logger.getLogger(this.getClass().getName());
            log.info(e.getMessage());
        }
        this.jFrame = jFrame;
        this.jFrame.setVisible(false);
        this.sound = sound;
        setLayout(null);
        setTitle("Music Player");
        setSize(300, 450);
//        setAlwaysOnTop(true);
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
            this.getContentPane().add(pauseBtn, Integer.valueOf(0));
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
            this.getContentPane().add(playBtn, Integer.valueOf(0));
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
        this.getContentPane().add(backBtn, Integer.valueOf(0));

        statusLabel = new JLabel(String.format("Status: %s, Volume: %.0f%%", this.sound.isPlaying() ? "Playing" : "Paused", this.sound.getVolume() * 100));
        statusLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBounds(10, 370, 200, 30);
        this.getContentPane().add(statusLabel, Integer.valueOf(0));

        volumeSlider = new JSlider(0, 100, (int) (this.sound.getVolume() * 100));
        volumeSlider.setBounds(10, 370, 200, 10);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setOpaque(false);
        volumeSlider.setFocusable(false);
        this.getContentPane().add(volumeSlider, Integer.valueOf(0));

        volumeSlider.addChangeListener(_ -> {
            double volume = volumeSlider.getValue() / 100.0;
            this.sound.setVolume(volume);
            statusLabel.setText(String.format("Status: %s, Volume: %.0f%%", this.sound.isPlaying() ? "Playing" : "Paused", volume * 100));
        });

        ImageIcon back = new ImageIcon("src/images/MusicFrameBackground.png");
        back.setImage(back.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
        JLabel bg = new JLabel(back);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1));

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
        JList<String> songList = getSongList();
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                this.getContentPane().remove(pauseBtn);
                this.getContentPane().add(playBtn, Integer.valueOf(0));
                sound.pause();
                sound.displayStatus();
                choose = songList.getSelectedIndex();
                String selectedSong = SongName[choose];
                sound.changeSource("src/misc/" + selectedSong);
                sound.setVolume(0.5);
                statusLabel.setText(String.format("Status: %s, Volume: %.0f%%", sound.isPlaying() ? "Playing" : "Paused", sound.getVolume() * 100));
                revalidate();
                repaint();
            }
        });
        songList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    remove(playBtn);
                    add(pauseBtn);
                    sound.play();
                    statusLabel.setText(String.format("Status: %s, Volume: %.0f%%", sound.isPlaying() ? "Playing" : "Paused", sound.getVolume() * 100));
                    volumeSlider.setValue(50);
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
        // SongList 的鼠标进入/离开效果
        songList.setCellRenderer((_, value, _, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value);
            label.setFont(new Font("Serif", Font.BOLD, 12));
            if (isSelected || cellHasFocus) {
                label.setOpaque(false);
                label.setForeground(Color.YELLOW);
            } else {
                label.setOpaque(false);
                label.setForeground(Color.WHITE);
            }
            return label;
        });
        songList.setOpaque(false);

        // 将列表放置在滚动面板中，并将滚动面板添加到悬浮窗口中
        JScrollPane scrollPane = new JScrollPane(songList);
        scrollPane.setBounds(35, 35, 220, 260);
        return scrollPane;
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        if (e.getSource() == backBtn) {
            this.setVisible(false);
            this.jFrame.setVisible(true);
        } else if (e.getSource() == playBtn) {
            this.getContentPane().remove(playBtn);
            this.getContentPane().add(pauseBtn);
            this.sound.play();
            this.statusLabel.setText(String.format("Status: %s, Volume: %.0f%%", this.sound.isPlaying() ? "Playing" : "Paused", this.sound.getVolume() * 100));
            this.sound.displayStatus();
            this.revalidate(); // 重新布局组件
            this.repaint();    // 重绘界面
        } else if (e.getSource() == pauseBtn) {
            this.getContentPane().remove(pauseBtn);
            this.getContentPane().add(playBtn);
            this.sound.pause();
            this.statusLabel.setText(String.format("Status: %s, Volume: %.0f%%", this.sound.isPlaying() ? "Playing" : "Paused", this.sound.getVolume() * 100));
            this.sound.displayStatus();
            this.revalidate(); // 重新布局组件
            this.repaint();    // 重绘界面
        }
    }
}
