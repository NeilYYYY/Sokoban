package view.level;

import org.jetbrains.annotations.NotNull;
import view.ParticlePanel;
import view.login.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SettingFrame extends JFrame implements ActionListener {
    JButton changeUsernameBtn;
    JButton changePasswordBtn;
    JButton backBtn;
    LevelFrame levelFrame;
    User user;
    JLabel changeUsername;

    public SettingFrame(LevelFrame levelFrame, @NotNull User user) {
        this.setTitle("Settings");
        this.setAlwaysOnTop(false);
        this.setLayout(null);//关闭默认布局类型 自己手动设置布局
        this.setSize(800, 450);
        this.setLocationRelativeTo(null);//设置GUI显示居中
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置关闭模式
        this.getContentPane().setLayout(null);
        this.setResizable(false);

        this.user = user;
        this.levelFrame = levelFrame;
        this.levelFrame.setVisible(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.setContentPane(layeredPane);

        ParticlePanel panel = new ParticlePanel(75, false, false);
        panel.setBounds(0, 0, 800, 450);
        panel.setOpaque(false);
        this.getContentPane().add(panel, Integer.valueOf(0));

        Font f = new Font("Comic Sans MS", Font.PLAIN, 13);
        this.changeUsername = new JLabel(String.format("Username: %s", user.getUsername()));
        this.changeUsername.setFont(f);
        this.changeUsername.setForeground(Color.WHITE);
        JLabel changePassword = new JLabel("Password: ********");
        changePassword.setFont(f);
        changePassword.setForeground(Color.WHITE);

        this.changeUsernameBtn = new JButton("Change");
        this.changeUsernameBtn.setFont(f);
        this.changeUsernameBtn.setFont(f.deriveFont(16f));
        this.changeUsernameBtn.setForeground(Color.WHITE);
        this.changeUsernameBtn.setMargin(new Insets(0, 0, 0, 0));
        this.changeUsernameBtn.setBorderPainted(false);
        this.changeUsernameBtn.setBorder(null);
        this.changeUsernameBtn.setFocusPainted(false);
        this.changeUsernameBtn.setContentAreaFilled(false);
        this.changeUsernameBtn.addActionListener(this);

        this.changePasswordBtn = new JButton("Change");
        this.changePasswordBtn.setFont(f);
        this.changePasswordBtn.setFont(f.deriveFont(16f));
        this.changePasswordBtn.setForeground(Color.WHITE);
        this.changePasswordBtn.setMargin(new Insets(0, 0, 0, 0));
        this.changePasswordBtn.setBorderPainted(false);
        this.changePasswordBtn.setBorder(null);
        this.changePasswordBtn.setFocusPainted(false);
        this.changePasswordBtn.setContentAreaFilled(false);
        this.changePasswordBtn.addActionListener(this);

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

        JPanel usernameJp = new JPanel();
        usernameJp.setBackground(null);
        usernameJp.setOpaque(false);
        JPanel passwordJp = new JPanel();
        passwordJp.setBackground(null);
        passwordJp.setOpaque(false);
        JPanel backJp = new JPanel();
        backJp.setBackground(null);
        backJp.setOpaque(false);

        usernameJp.setBounds(200, 145, 400, 40);
        passwordJp.setBounds(200, 205, 400, 40);
        backJp.setBounds(300, 280, 200, 50);

        usernameJp.add(changeUsername);
        usernameJp.add(changeUsernameBtn);
        passwordJp.add(changePassword);
        passwordJp.add(changePasswordBtn);
        backJp.add(backBtn);

        this.getContentPane().add(usernameJp, Integer.valueOf(1));
        this.getContentPane().add(passwordJp, Integer.valueOf(1));
        this.getContentPane().add(backJp, Integer.valueOf(1));

        ImageIcon back = new ImageIcon("src/images/Menu_Theme_Godmaster.png");
        back.setImage(back.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT));
        JLabel bg = new JLabel(back);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        if (e.getSource() == backBtn) {
            this.dispose();
            this.levelFrame.setVisible(true);
        } else if (e.getSource() == changeUsernameBtn) {
            String newUsername = JOptionPane.showInputDialog("Please enter your new username:");
            if (newUsername != null) {
                ArrayList<User> users = User.getUserList();
                users.get(this.user.getId()).setUsername(newUsername);
                User.writeUser(users);
                this.changeUsername.setText(String.format("Username: %s", newUsername));
            }
        } else if (e.getSource() == changePasswordBtn) {
            String newPassword = JOptionPane.showInputDialog("Please enter your new password:");
            if (newPassword != null) {
                ArrayList<User> users = User.getUserList();
                try {
                    users.get(this.user.getId()).setPassword(User.getSHA(newPassword));
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
                User.writeUser(users);
            }
        }
    }
}
