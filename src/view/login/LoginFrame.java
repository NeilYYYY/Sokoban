package view.login;

import org.jetbrains.annotations.NotNull;
import view.ParticlePanel;
import view.level.LevelFrame;
import view.music.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;

public class LoginFrame extends JFrame implements ActionListener {

    private final JTextField usernameText;
    private final JPasswordField passwordText;
    private final JButton loginBtn;
    private final JButton registerBtn;
    private final JButton musicBtn;
    private final JButton guestBtn;
    private final Sound sound;
    private LevelFrame levelFrame;

    public LoginFrame(Sound sound) {
        this.setTitle("Sokoban Knight");
        this.setAlwaysOnTop(false);
        this.setLayout(null);//关闭默认布局类型 自己手动设置布局
        this.setSize(800, 450);
        this.setLocationRelativeTo(null);//设置GUI显示居中
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置关闭模式
        this.getContentPane().setLayout(null);
        this.setResizable(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.setContentPane(layeredPane);

        ParticlePanel panel = new ParticlePanel(75, false, false);
        panel.setBounds(0, 0, 800, 450);
        panel.setOpaque(false);
        this.getContentPane().add(panel, Integer.valueOf(0));
        this.sound = sound;
        JLabel username = new JLabel("Username: ");
        Font f = new Font("Comic Sans MS", Font.PLAIN, 13);
        username.setFont(f);
        username.setForeground(Color.WHITE);
        JLabel password = new JLabel("Password: ");
        password.setFont(f);
        password.setForeground(Color.WHITE);
        JLabel titleLabel = new JLabel(new ImageIcon("src/images/Title.png"));
        titleLabel.setBounds(240, 20, 300, 155);
        this.getContentPane().add(titleLabel, Integer.valueOf(1));

        guestBtn = new JButton("Guest");
        guestBtn.setFont(f);
        guestBtn.setForeground(Color.GRAY);
        guestBtn.setMargin(new Insets(0, 0, 0, 0));
        guestBtn.setBorderPainted(false);
        guestBtn.setBorder(null);
        guestBtn.setFocusPainted(false);
        guestBtn.setContentAreaFilled(false);
        guestBtn.addActionListener(this);
        guestBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                guestBtn.setForeground(Color.DARK_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                guestBtn.setForeground(Color.GRAY);
            }
        });
        loginBtn = new JButton("Login");
        loginBtn.setFont(f);
        loginBtn.setFont(f.deriveFont(16f));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setMargin(new Insets(0, 0, 0, 0));
        loginBtn.setBorderPainted(false);
        loginBtn.setBorder(null);
        loginBtn.setFocusPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.addActionListener(this);//监听登录事件
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginBtn.setForeground(Color.WHITE);
            }
        });
        registerBtn = new JButton("Register");
        registerBtn.setFont(f);
        registerBtn.setFont(f.deriveFont(16f));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setMargin(new Insets(0, 0, 0, 0));
        registerBtn.setBorderPainted(false);
        registerBtn.setBorder(null);
        registerBtn.setFocusPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.addActionListener(this);//监听注册事件
        registerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerBtn.setForeground(Color.WHITE);
            }
        });
        musicBtn = new JButton("Music");
        musicBtn.setFont(f);
        musicBtn.setForeground(Color.WHITE);
        musicBtn.setMargin(new Insets(0, 0, 0, 0));
        musicBtn.setBorderPainted(false);
        musicBtn.setBorder(null);
        musicBtn.setFocusPainted(false);
        musicBtn.setContentAreaFilled(false);
        musicBtn.addActionListener(this);
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

        usernameText = new JTextField(15);
        passwordText = new JPasswordField(15);

        usernameText.setForeground(Color.BLACK);
        passwordText.setForeground(Color.BLACK);
        usernameText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        passwordText.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        usernameText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginBtn.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        passwordText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginBtn.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        JPanel usernameJp = new JPanel();
        usernameJp.setBackground(null);
        usernameJp.setOpaque(false);
        JPanel passwordJp = new JPanel();
        passwordJp.setBackground(null);
        passwordJp.setOpaque(false);
        JPanel loginJp = new JPanel();
        loginJp.setBackground(null);
        loginJp.setOpaque(false);
        JPanel registerJp = new JPanel();
        registerJp.setBackground(null);
        registerJp.setOpaque(false);
        JPanel musicJp = new JPanel();
        musicJp.setBackground(null);
        musicJp.setOpaque(false);
        JPanel guestJp = new JPanel();
        guestJp.setBackground(null);
        guestJp.setOpaque(false);

        usernameJp.setBounds(200, 210, 400, 50);
        passwordJp.setBounds(200, 250, 400, 50);
        loginJp.setBounds(300, 300, 100, 60);
        registerJp.setBounds(390, 300, 100, 60);
        musicJp.setBounds(this.getWidth() - 130, this.getHeight() - 90, 100, 60);
        guestJp.setBounds(345, 340, 100, 60);
        usernameJp.add(username);
        usernameJp.add(usernameText);
        passwordJp.add(password);
        passwordJp.add(passwordText);
        loginJp.add(loginBtn);
        registerJp.add(registerBtn);
        musicJp.add(musicBtn);
        guestJp.add(guestBtn);

        this.getContentPane().add(usernameJp, Integer.valueOf(1));
        this.getContentPane().add(passwordJp, Integer.valueOf(1));
        this.getContentPane().add(loginJp, Integer.valueOf(1));
        this.getContentPane().add(registerJp, Integer.valueOf(1));
        this.getContentPane().add(musicJp, Integer.valueOf(1));
        this.getContentPane().add(guestJp, Integer.valueOf(1));
        ImageIcon back = new ImageIcon("src/images/Menu_Theme_Godmaster.png");
        back.setImage(back.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
        JLabel bg = new JLabel(back);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层
        setVisible(true);
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());
            // 根据读取的用户账号信息进行校验
            boolean temp;
            try {
                temp = User.checkUser(username, User.getSHA(password));
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(temp);
            User user;
            if (temp && !username.isEmpty()) {
                user = User.getUser(usernameText.getText(), User.getUserList());
                levelFrame = new LevelFrame(user, this.sound, false, false);
                JOptionPane.showMessageDialog(this, "登录成功喵～", "Success", JOptionPane.INFORMATION_MESSAGE);
                showLevelFrame();
            } else {
                JOptionPane.showMessageDialog(this, "登录失败喵！", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }//注册操作
        else if (e.getSource() == registerBtn) {
            new Register(this);
        } else if (e.getSource() == musicBtn) {
            new MusicFrame(this, this.sound);
        } else if (e.getSource() == guestBtn) {
            User user;
            user = User.getUser("", User.getUserList());
            levelFrame = new LevelFrame(user, this.sound, false, false);
            JOptionPane.showMessageDialog(this, "游客模式喵～", "Info", JOptionPane.INFORMATION_MESSAGE);
            showLevelFrame();
        }
    }

    private void showLevelFrame() {
        this.dispose();
        System.out.println("用户名 = " + usernameText.getText() + "喵");
        System.out.println("密码 = " + String.valueOf(passwordText.getPassword()) + "喵");
        try {
            System.out.println("密码SHA = " + User.getSHA(String.valueOf(passwordText.getPassword())) + "喵");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        levelFrame.setVisible(true);
    }
}
