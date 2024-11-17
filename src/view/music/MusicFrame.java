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
    private final JPanel contentPane;
    private final JButton playBtn;
    private final JButton pauseBtn;
    private final JButton backBtn;
    private final Sound sound;
    private int choose;
    private final String[] SongName;

    public MusicFrame(JFrame jFrame, Sound sound) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.jFrame = jFrame;
        this.jFrame.setVisible(false);
        choose = 4;
        SongName = new String[]{"Alphys.wav", "EnterHallownest.wav", "Main_Theme.wav", "恋ひ恋ふ縁.wav"};
        setLayout(null);
        setTitle("Music Player");
        setSize(300, 450);
        setAlwaysOnTop(true);//设置界面一直处于最上层
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);

        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // 绘制背景图片
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/images/w.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        contentPane.setBounds(35, 35, 220, 260);
        // 创建一个列表模型
        DefaultListModel<String> listModel = new DefaultListModel<>();
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
                    choose = songList.getSelectedIndex();
                    String selectedSong = SongName[choose];
                    sound.changeSource("src/misc/" + selectedSong);
                    add(playBtn);
                    remove(pauseBtn);
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
                    revalidate();
                    repaint();
                    sound.start(true);
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
        //scrollPane.setOpaque(false);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        add(contentPane);

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
//                remove(blank);
                add(playBtn);
                sound.stop();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                pauseBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
        });
        if (sound.isPlaying()) {
            add(this.pauseBtn);
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
                sound.continues();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                playBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
        });
        if (!sound.isPlaying()) {
            add(this.playBtn);
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
        add(this.backBtn);


        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            this.setVisible(false);
            jFrame.setVisible(true);
        }
    }
}
