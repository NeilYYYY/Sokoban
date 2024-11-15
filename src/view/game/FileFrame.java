package view.game;

import model.Sound;
import view.level.LevelFrame;
import view.login.Register;
import view.login.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;


public class FileFrame extends JFrame /*implements ActionListener */ {
    private User user;
    private GameFrame gameFrame;
    private int lv;
    private String filePath;

    public FileFrame(int width, int height, User user, GameFrame gameframe, int lv) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.user = user;
        this.gameFrame = gameframe;
        this.lv = lv;
        this.filePath = String.format("src/saves/%d-%d.json", this.lv, this.user.getId());
        this.setTitle("Savings");
        this.setAlwaysOnTop(false);
        this.setLayout(null);//关闭默认布局类型 自己手动设置布局
        this.setSize(width, height);
        this.setLocationRelativeTo(null);//设置GUI显示居中
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置关闭模式
        this.getContentPane().setLayout(null);
        //创建界面组件
        JLabel[][] files = new JLabel[2][1];
        for (int i = 0; i < files.length; i++) {
            for (int j = 0; j < files[0].length; j++) {
                files[i][j] = new JLabel("File: " + (i * 3 + j));
            }
        }
        JPanel[][] panelsJP = new JPanel[2][3];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                panelsJP[i][j] = new JPanel();
                panelsJP[i][j].setBackground(null);
                panelsJP[i][j].setOpaque(false);
                panelsJP[i][j].setBounds((i - 1) * 100 + 50, (j - 1) * 100 + 50, 200, 50);
            }
        }
        /*
        for (int i = 0; i < 2; i++) {}
        JLabel username = new JLabel("Username(Empty is Guest)：");
        JLabel password = new JLabel("Password：");
        loginBtn = new JButton("Login");
        loginBtn.addActionListener(this);//监听登录事件
        registerBtn = new JButton("Register");
        registerBtn.addActionListener(this);//监听注册事件
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
        usernameJp.setBounds(200, 225, 400, 50);
        passwordJp.setBounds(300, 265, 200, 50);
        loginJp.setBounds(300, 310, 200, 60);
        usernameJp.add(username);
        usernameJp.add(usernameText);
        passwordJp.add(password);
        passwordJp.add(passwordText);
        loginJp.add(loginBtn);
        loginJp.add(registerBtn);
        //将组件装入GUI
        add(usernameJp);
        add(passwordJp);
        add(loginJp);
        JLabel rec = new JLabel();
        rec.setBounds(250, 210, 300, 150);
        rec.setOpaque(true);
        rec.setBackground(Color.WHITE);
        this.getContentPane().add(rec);
        JLabel bg = new JLabel(new ImageIcon("src/images/1.jpg"));
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg);
        setVisible(true);
    }
    */
        /*
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());
            // 根据读取的用户账号信息进行校验
            boolean temp = readUser(username, password);
            System.out.println(temp);
            if (temp) {
                LevelFrame levelFrame;
                User user;
                if (username.isEmpty()) {
                    user = User.getUser("", User.getUserList());
                    levelFrame = new LevelFrame(510, 200, user);
                    levelFrame.setVisible(false);
                    JOptionPane.showMessageDialog(this, "游客模式", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    System.out.println("Username = " + usernameText.getText());
                    System.out.println("Password = " + Arrays.toString(passwordText.getPassword()));
                    levelFrame.setVisible(true);
                    this.setVisible(false);
                    this.sound.stop();
                    return;
                }
                user = User.getUser(usernameText.getText(), User.getUserList());
                levelFrame = new LevelFrame(510, 200, user);
                levelFrame.setVisible(false);
                JOptionPane.showMessageDialog(this, "登录成功", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();//登录成功关闭此窗口 跳转页面
                System.out.println("Username = " + usernameText.getText());
                System.out.println("Password = " + Arrays.toString(passwordText.getPassword()));
                levelFrame.setVisible(true);
                this.setVisible(false);
                this.sound.stop();
            } else {
                JOptionPane.showMessageDialog(this, "登陆失败", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }//注册操作
        else if (e.getSource() == registerBtn) {
            this.dispose();
            new Register();
        }


         */
    }

}
