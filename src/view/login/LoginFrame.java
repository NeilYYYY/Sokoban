package view.login;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Sound;
import view.level.LevelFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LoginFrame extends JFrame implements ActionListener {

    private final JTextField usernameText;
    private final JPasswordField passwordText;
    private final JButton loginBtn;
    private final JButton registerBtn;
    private final Sound sound;
    private LevelFrame levelFrame;
    private User user;

    public LoginFrame() {
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
        this.sound = new Sound("src/misc/Main_Theme.wav");
        sound.start(true);
        //创建界面组件
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
        rec.setBackground(new Color(255, 255, 255, 200));
        this.getContentPane().add(rec);
        JLabel bg = new JLabel(new ImageIcon("src/images/1.jpg"));
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg);
        setVisible(true);
    }

    public static String getSHA(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] shaBytes = md.digest(str.getBytes());
        return bytesToHexString(shaBytes);
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        for (byte aByte : bytes) {
            int v = aByte & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());
            // 根据读取的用户账号信息进行校验
            boolean temp;
            try {
                temp = readUser(username, getSHA(password));
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(temp);
            if (username.isEmpty()) {
                user = User.getUser("", User.getUserList());
                levelFrame = new LevelFrame(user);
                JOptionPane.showMessageDialog(this, "游客模式", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                levelFrame.getSound().start(true);
                System.out.println("Username = " + usernameText.getText());
                System.out.println("Password = " + String.valueOf(passwordText.getPassword()));
                try {
                    System.out.println("Password.SHA = " + getSHA(String.valueOf(passwordText.getPassword())));
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
                levelFrame.setVisible(true);
                this.sound.stop();
                return;
            }
            if (temp) {
                user = User.getUser(usernameText.getText(), User.getUserList());
                levelFrame = new LevelFrame(user);
                JOptionPane.showMessageDialog(this, "登录成功", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();//登录成功关闭此窗口 跳转页面
                levelFrame.getSound().start(true);
                System.out.println("Username = " + usernameText.getText());
                System.out.println("Password = " + String.valueOf(passwordText.getPassword()));
                try {
                    System.out.println("Password.SHA = " + getSHA(String.valueOf(passwordText.getPassword())));
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
                levelFrame.setVisible(true);
                this.sound.stop();
            } else {
                JOptionPane.showMessageDialog(this, "登录失败", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }//注册操作
        else if (e.getSource() == registerBtn) {
            new Register(this);
        }
    }

    //读取用户数据
    public boolean readUser(String username, String password) {

        try (BufferedReader br = new BufferedReader(new FileReader("src/users.json"))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line).append("\n");
            }
            Gson gson = new Gson();
            ArrayList<User> dataList = gson.fromJson(json.toString(), new TypeToken<ArrayList<User>>() {
            }.getType());
            for (User data : dataList) {
                if (data.getUsername().equals(username) && data.getPassword().equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
