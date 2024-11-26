package view.login;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Register extends JFrame implements ActionListener {
    private final JTextField usernameText;
    private final JPasswordField passwordText;
    private final JPasswordField passwordTextTrue;
    private final JButton registerBtn;
    private final JButton backBtn;
    private final LoginFrame loginFrame;

    public Register(LoginFrame loginFrame) {
        try {
            String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            Logger log = Logger.getLogger(getClass().getName());
            log.info(e.getMessage());
        }
        this.loginFrame = loginFrame;
        this.loginFrame.setVisible(false);
        setLayout(null);
        setTitle("Register");
        setSize(800, 450);
//        setAlwaysOnTop(true);//设置界面一直处于最上层
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        this.setResizable(false);

        //组件
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.setContentPane(layeredPane);

        GlowingParticleEffect panel = new GlowingParticleEffect(75);
        panel.setBounds(0, 0, 800, 450);
        panel.setOpaque(false);
        this.getContentPane().add(panel, Integer.valueOf(0));
        JLabel username = new JLabel("Username:");
        Font f = new Font("Comic Sans MS", Font.PLAIN, 13);
        username.setFont(f);
        username.setForeground(Color.WHITE);
        JLabel password = new JLabel("Password:");
        password.setFont(f);
        password.setForeground(Color.WHITE);
        JLabel passwordTrue = new JLabel("Confirm Password:");
        passwordTrue.setFont(f);
        passwordTrue.setForeground(Color.WHITE);
        registerBtn = new JButton("Register");
        registerBtn.setFont(f);
        registerBtn.setFont(f.deriveFont(16f));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setMargin(new Insets(0, 0, 0, 0));
        registerBtn.setBorderPainted(false);
        registerBtn.setBorder(null);
        registerBtn.setFocusPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.addActionListener(this);
        backBtn = new JButton("Back");
        backBtn.setFont(f);
        backBtn.setFont(f.deriveFont(16f));
        backBtn.setForeground(Color.WHITE);
        backBtn.setMargin(new Insets(0, 0, 0, 0));
        backBtn.setBorderPainted(false);
        backBtn.setBorder(null);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.addActionListener(this);
        usernameText = new JTextField(15);
        passwordText = new JPasswordField(15);
        passwordTextTrue = new JPasswordField(15);
        // 设置字体和背景颜色
        usernameText.setForeground(Color.BLACK);
        passwordText.setForeground(Color.BLACK);
        passwordTextTrue.setForeground(Color.BLACK);
        usernameText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        passwordText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        passwordTextTrue.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        passwordTextTrue.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    registerBtn.doClick(); // 模拟按下注册按钮
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        //创建装组件的容器
        JPanel usernameJp = new JPanel();
        usernameJp.setBackground(null);
        usernameJp.setOpaque(false);
        JPanel passwordJp = new JPanel();
        passwordJp.setBackground(null);
        passwordJp.setOpaque(false);
        JPanel passwordTrueJp = new JPanel();
        passwordTrueJp.setBackground(null);
        passwordTrueJp.setOpaque(false);
        JPanel backJp = new JPanel();
        backJp.setBackground(null);
        backJp.setOpaque(false);
        JPanel registerJp = new JPanel();
        registerJp.setBackground(null);
        registerJp.setOpaque(false);
        //设置容器的位置
        usernameJp.setBounds(200, 125, 400, 40);
        passwordJp.setBounds(200, 175, 400, 40);
        passwordTrueJp.setBounds(200, 225, 400, 40);
        backJp.setBounds(240, 280, 200, 50);
        registerJp.setBounds(350, 280, 200, 50);

        usernameJp.add(username);
        usernameJp.add(usernameText);
        passwordJp.add(password);
        passwordJp.add(passwordText);
        passwordTrueJp.add(passwordTrue);
        passwordTrueJp.add(passwordTextTrue);
        backJp.add(backBtn);
        registerJp.add(registerBtn);
        //将组件装入GUI
        this.getContentPane().add(usernameJp, Integer.valueOf(1));
        this.getContentPane().add(passwordJp, Integer.valueOf(1));
        this.getContentPane().add(passwordTrueJp, Integer.valueOf(1));
        this.getContentPane().add(backJp, Integer.valueOf(1));
        this.getContentPane().add(registerJp, Integer.valueOf(1));
        ImageIcon back = new ImageIcon("src/images/Menu_Theme_Godmaster.png");
        back.setImage(back.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
        JLabel bg = new JLabel(back);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层
        setVisible(true);
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        if (e.getSource() == registerBtn) {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());
            String passwordTrue = new String(passwordTextTrue.getPassword());
            ArrayList<User> user = User.getUserList();
            for (User data : user) {
                System.out.println(data.username());
            }
            boolean found;
            found = (!username.isEmpty() && !password.isEmpty() && !passwordTrue.isEmpty() && password.equals(passwordTrue));
            System.out.println(found);
            if (found) {
                boolean temp = User.readUser(username, user);//检测用户名是否重复
                if (temp) {
                    JOptionPane.showMessageDialog(this, "注册成功", "Success", JOptionPane.INFORMATION_MESSAGE);
                    int id = user.toArray().length;
                    try {
                        user.add(new User(id, username, User.getSHA(password), new boolean[]{false, false, false, false, false, false}));
                    } catch (NoSuchAlgorithmException ex) {
                        throw new RuntimeException(ex);
                    }
                    User.writeUser(user);//将新用户的数据写入json表中
                    this.dispose();
                    this.loginFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "用户名重复，注册失败", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "注册失败,请正确填写用户名和密码", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == backBtn) {
            this.dispose();
            this.loginFrame.setVisible(true);
        }
    }
}
