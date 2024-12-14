package view.login;

import org.jetbrains.annotations.NotNull;
import view.ParticlePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Register extends JFrame implements ActionListener {
    private final JTextField usernameText;
    private final JPasswordField passwordText;
    private final JPasswordField passwordTextTrue;
    private final JButton registerBtn;
    private final JButton backBtn;
    private final LoginFrame loginFrame;

    public Register(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.loginFrame.setVisible(false);
        setLayout(null);
        setTitle("Register");
        setSize(800, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        this.setResizable(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.setContentPane(layeredPane);

        ParticlePanel panel = new ParticlePanel(75, false, false);
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
        this.registerBtn = new JButton("Register");
        this.registerBtn.setFont(f);
        this.registerBtn.setFont(f.deriveFont(16f));
        this.registerBtn.setForeground(Color.WHITE);
        this.registerBtn.setMargin(new Insets(0, 0, 0, 0));
        this.registerBtn.setBorderPainted(false);
        this.registerBtn.setBorder(null);
        this.registerBtn.setFocusPainted(false);
        this.registerBtn.setContentAreaFilled(false);
        this.registerBtn.addActionListener(this);
        this.registerBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerBtn.setForeground(Color.WHITE);
            }
        });
        this.backBtn = new JButton("Back");
        this.backBtn.setFont(f);
        this.backBtn.setFont(f.deriveFont(16f));
        this.backBtn.setForeground(Color.WHITE);
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
        this.usernameText = new JTextField(15);
        this.passwordText = new JPasswordField(15);
        this.passwordTextTrue = new JPasswordField(15);

        this.usernameText.setForeground(Color.BLACK);
        this.passwordText.setForeground(Color.BLACK);
        this.passwordTextTrue.setForeground(Color.BLACK);

        this.usernameText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.passwordText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.passwordTextTrue.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        this.passwordTextTrue.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    registerBtn.doClick();
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
        JPanel passwordTrueJp = new JPanel();
        passwordTrueJp.setBackground(null);
        passwordTrueJp.setOpaque(false);
        JPanel backJp = new JPanel();
        backJp.setBackground(null);
        backJp.setOpaque(false);
        JPanel registerJp = new JPanel();
        registerJp.setBackground(null);
        registerJp.setOpaque(false);

        usernameJp.setBounds(200, 125, 400, 40);
        passwordJp.setBounds(200, 175, 400, 40);
        passwordTrueJp.setBounds(200, 225, 400, 40);
        backJp.setBounds(240, 280, 200, 50);
        registerJp.setBounds(350, 280, 200, 50);

        usernameJp.add(username);
        usernameJp.add(this.usernameText);
        passwordJp.add(password);
        passwordJp.add(this.passwordText);
        passwordTrueJp.add(passwordTrue);
        passwordTrueJp.add(this.passwordTextTrue);
        backJp.add(this.backBtn);
        registerJp.add(this.registerBtn);

        this.getContentPane().add(usernameJp, Integer.valueOf(1));
        this.getContentPane().add(passwordJp, Integer.valueOf(1));
        this.getContentPane().add(passwordTrueJp, Integer.valueOf(1));
        this.getContentPane().add(backJp, Integer.valueOf(1));
        this.getContentPane().add(registerJp, Integer.valueOf(1));

        ImageIcon back = new ImageIcon("resources/images/Menu_Theme_Godmaster.png");
        back.setImage(back.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
        JLabel bg = new JLabel(back);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层
        setVisible(true);
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        if (e.getSource() == this.registerBtn) {
            String username = this.usernameText.getText();
            String password = new String(this.passwordText.getPassword());
            String passwordTrue = new String(this.passwordTextTrue.getPassword());
            ArrayList<User> users = User.getUserList();
            for (User data : users) {
                System.out.println(data.getUsername());
            }
            boolean found;
            found = (!username.isEmpty() && !password.isEmpty() && !passwordTrue.isEmpty() && password.equals(passwordTrue) && !username.equals("Deleted"));
            if (found) {
                boolean temp = User.checkUsername(username, users);//检测用户名是否重复
                if (temp) {
                    int id = users.toArray().length;
                    User tempUser = User.getUser("Deleted", users);
                    if (tempUser != null) {
                        id = tempUser.getId();
                        try {
                            users.set(id, new User(id, username, User.getSHA(password), User.createDefaultLv(false)));
                        } catch (NoSuchAlgorithmException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        try {
                            users.add(new User(id, username, User.getSHA(password), User.createDefaultLv(false)));
                        } catch (NoSuchAlgorithmException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    User.writeUser(users);//将新用户的数据写入json表中
                    JOptionPane.showMessageDialog(this, "注册成功喵～", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    this.loginFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "用户名重复了喵！！！", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "好好填用户名和密码喵！", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == this.backBtn) {
            this.dispose();
            this.loginFrame.setVisible(true);
        }
    }
}
