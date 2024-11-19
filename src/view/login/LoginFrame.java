package view.login;

import view.level.LevelFrame;
import view.music.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;

public class LoginFrame extends JFrame implements ActionListener {

    private final JTextField usernameText;
    private final JPasswordField passwordText;
    private final JButton loginBtn;
    private final JButton registerBtn;
    private final JButton musicBtn;
    private final Sound sound;
    private final Font f = new Font("Comic Sans MS", Font.PLAIN, 13);
    private LevelFrame levelFrame;

    public LoginFrame(Sound sound) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTitle("Sokoban");
        this.setAlwaysOnTop(false);
        this.setLayout(null);//关闭默认布局类型 自己手动设置布局
        this.setSize(800, 450);
        this.setLocationRelativeTo(null);//设置GUI显示居中
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置关闭模式
        this.getContentPane().setLayout(null);
        this.setResizable(false);
        //创建界面组件
        this.sound = sound;
        JLabel username = new JLabel("Username(Empty is Guest): ");
        username.setFont(f);
        username.setForeground(Color.WHITE);
        JLabel password = new JLabel("Password: ");
        password.setFont(f);
        password.setForeground(Color.WHITE);
        JLabel titleLabel = new JLabel(new ImageIcon("src/images/Title.png"));
        titleLabel.setBounds(250, 20, 300, 155);
        this.add(titleLabel);
        loginBtn = new JButton("Login");
        loginBtn.setFont(f);
        loginBtn.addActionListener(this);//监听登录事件
        registerBtn = new JButton("Register");
        registerBtn.setFont(f);
        registerBtn.addActionListener(this);//监听注册事件
        musicBtn = new JButton("Music");
        musicBtn.setFont(f);
        musicBtn.addActionListener(this);
        usernameText = new JTextField(15);
        passwordText = new JPasswordField(15);
        // 设置字体和背景颜色
        usernameText.setForeground(Color.BLACK);
        passwordText.setForeground(Color.BLACK);
        usernameText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        passwordText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        //创建装组件的容器
        JPanel usernameJp = new JPanel();
        usernameJp.setBackground(null);
        usernameJp.setOpaque(false);
        JPanel passwordJp = new JPanel();
        passwordJp.setBackground(null);
        passwordJp.setOpaque(false);
        JPanel loginJp = new JPanel();
        loginJp.setBackground(null);
        loginJp.setOpaque(false);
        //设置容器的位置
        usernameJp.setBounds(200, 210, 400, 50);
        passwordJp.setBounds(300, 250, 200, 50);
        loginJp.setBounds(250, 310, 300, 60);
        usernameJp.add(username);
        usernameJp.add(usernameText);
        passwordJp.add(password);
        passwordJp.add(passwordText);
        loginJp.add(loginBtn);
        loginJp.add(registerBtn);
        loginJp.add(musicBtn);
        //将组件装入GUI
        add(usernameJp);
        add(passwordJp);
        add(loginJp);
        JLabel bg = new JLabel(new ImageIcon("src/images/Menu_Theme_Godmaster.png"));
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.add(bg);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
            if (username.isEmpty()) {
                user = User.getUser("", User.getUserList());
                levelFrame = new LevelFrame(user, this.sound);
                JOptionPane.showMessageDialog(this, "游客模式", "Success", JOptionPane.INFORMATION_MESSAGE);
                showLevelFrame();
                return;
            }
            if (temp) {
                user = User.getUser(usernameText.getText(), User.getUserList());
                levelFrame = new LevelFrame(user, this.sound);
                JOptionPane.showMessageDialog(this, "登录成功", "Success", JOptionPane.INFORMATION_MESSAGE);
                showLevelFrame();
            } else {
                JOptionPane.showMessageDialog(this, "登录失败", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }//注册操作
        else if (e.getSource() == registerBtn) {
            new Register(this);
        } else if (e.getSource() == musicBtn) {
            new MusicFrame(this, this.sound);
        }
    }

    private void showLevelFrame() {
        this.dispose();
        System.out.println("Username = " + usernameText.getText());
        System.out.println("Password = " + String.valueOf(passwordText.getPassword()));
        try {
            System.out.println("Password.SHA = " + User.getSHA(String.valueOf(passwordText.getPassword())));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        levelFrame.setVisible(true);
    }
}
