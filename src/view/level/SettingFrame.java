package view.level;

import org.jetbrains.annotations.NotNull;
import view.ParticlePanel;
import view.login.LoginFrame;
import view.login.User;
import view.music.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SettingFrame extends JFrame implements ActionListener {
    JButton changeUsernameBtn;
    JButton changePasswordBtn;
    JButton backBtn;
    JButton deleteBtn;
    LevelFrame levelFrame;
    User user;
    JLabel changeUsername;
    Sound sound;

    public SettingFrame(LevelFrame levelFrame, @NotNull User user, Sound sound) {
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
        this.sound = sound;

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
        this.changeUsernameBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                changeUsernameBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                changeUsernameBtn.setForeground(Color.WHITE);
            }
        });

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
        this.changePasswordBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                changePasswordBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                changePasswordBtn.setForeground(Color.WHITE);
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

        this.deleteBtn = new JButton("Delete");
        this.deleteBtn.setFont(f);
        this.deleteBtn.setFont(f.deriveFont(16f));
        this.deleteBtn.setForeground(Color.WHITE);
        this.deleteBtn.setMargin(new Insets(0, 0, 0, 0));
        this.deleteBtn.setBorderPainted(false);
        this.deleteBtn.setBorder(null);
        this.deleteBtn.setFocusPainted(false);
        this.deleteBtn.setContentAreaFilled(false);
        this.deleteBtn.addActionListener(this);
        this.deleteBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                deleteBtn.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                deleteBtn.setForeground(Color.WHITE);
            }
        });

        JPanel usernameJp = new JPanel();
        usernameJp.setBackground(null);
        usernameJp.setOpaque(false);
        JPanel passwordJp = new JPanel();
        passwordJp.setBackground(null);
        passwordJp.setOpaque(false);
        JPanel backJp = new JPanel();
        backJp.setBackground(null);
        backJp.setOpaque(false);
        JPanel deleteJp = new JPanel();
        deleteJp.setBackground(null);
        deleteJp.setOpaque(false);

        usernameJp.setBounds(200, 145, 400, 40);
        passwordJp.setBounds(200, 205, 400, 40);
        backJp.setBounds(250, 280, 200, 50);
        deleteJp.setBounds(350, 280, 200, 50);

        usernameJp.add(changeUsername);
        usernameJp.add(changeUsernameBtn);
        passwordJp.add(changePassword);
        passwordJp.add(changePasswordBtn);
        backJp.add(backBtn);
        deleteJp.add(deleteBtn);

        this.getContentPane().add(usernameJp, Integer.valueOf(1));
        this.getContentPane().add(passwordJp, Integer.valueOf(1));
        this.getContentPane().add(backJp, Integer.valueOf(1));
        this.getContentPane().add(deleteJp, Integer.valueOf(1));

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
            String newUsername = JOptionPane.showInputDialog("输入你的新用户名喵：");

            if (newUsername == null) {
                // 用户点击了取消
                return;
            }

            // 用户名非空检测
            if (newUsername.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "用户名不能为空喵！！！", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<User> users = User.getUserList();
            users.get(this.user.getId()).setUsername(newUsername);
            User.writeUser(users);

            // 更新界面上的用户名
            this.changeUsername.setText(String.format("Username: %s", newUsername));
            JOptionPane.showMessageDialog(this, "修改用户名成功喵～");
        } else if (e.getSource() == changePasswordBtn) {
            String newPassword = JOptionPane.showInputDialog("输入你的新密码喵：");

            if (newPassword == null) {
                // 用户点击了取消
                return;
            }

            // 密码非空检测
            if (newPassword.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "密码不能为空喵！！！", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String confirmPassword = JOptionPane.showInputDialog("确认你的新密码喵：");

            if (confirmPassword == null) {
                // 用户点击了取消
                return;
            }

            // 确认密码输入
            if (confirmPassword.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "确认密码不能为空喵！！！", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (confirmPassword.equals(newPassword)) {
                try {
                    ArrayList<User> users = User.getUserList();
                    users.get(this.user.getId()).setPassword(User.getSHA(newPassword));  // 将新密码进行哈希处理
                    User.writeUser(users);
                    JOptionPane.showMessageDialog(this, "修改密码成功喵！");
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "密码不匹配喵～", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == deleteBtn) {
            Object[] options = {"不了喵～", "就是要删除喵！"};
            int confirm = JOptionPane.showOptionDialog(this, "你真的要删除账号喵？", "Delete Account", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if (confirm == 1) {
                ArrayList<User> users = User.getUserList();
                users.remove(this.user.getId());  // 删除当前用户
                User.writeUser(users);
                JOptionPane.showMessageDialog(this, "既然如此，你再也看不到你的账号了喵！！！", "Account Deleted", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                LoginFrame loginFrame = new LoginFrame(this.sound);
                loginFrame.setVisible(true);
            }
        }
    }
}
