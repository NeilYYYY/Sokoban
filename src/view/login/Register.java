package view.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Register extends JFrame implements ActionListener {

    private final JTextField usernameText;
    private final JPasswordField passwordText;
    private final JPasswordField passwordTextTrue;
    private final JButton registerBtn;
    private final JButton backBtn;
    private final LoginFrame loginFrame;

    public Register(LoginFrame loginFrame) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.loginFrame = loginFrame;
        this.loginFrame.setVisible(false);
        setLayout(null);
        setTitle("Register");
        setSize(800, 450);
        setAlwaysOnTop(true);//设置界面一直处于最上层
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        //组件
        JLabel username = new JLabel("Username:");
        JLabel password = new JLabel("Password:");
        JLabel passwordTrue = new JLabel("Confirm Password:");
        registerBtn = new JButton("Register");
        registerBtn.addActionListener(this);
        backBtn = new JButton("Back");
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
        JPanel registerJp = new JPanel();
        registerJp.setBackground(null);
        registerJp.setOpaque(false);
        //设置容器的位置
        usernameJp.setBounds(300, 125, 200, 40);
        passwordJp.setBounds(300, 175, 200, 40);
        passwordTrueJp.setBounds(250, 225, 300, 40);
        registerJp.setBounds(300, 280, 200, 50);

        usernameJp.add(username);
        usernameJp.add(usernameText);
        passwordJp.add(password);
        passwordJp.add(passwordText);
        passwordTrueJp.add(passwordTrue);
        passwordTrueJp.add(passwordTextTrue);
        registerJp.add(backBtn);
        registerJp.add(registerBtn);
        //将组件装入GUI
        add(usernameJp);
        add(passwordJp);
        add(passwordTrueJp);
        add(registerJp);
        JLabel rec = new JLabel();
        rec.setBounds(250, 50, 300, 350);
        rec.setOpaque(true);
        rec.setBackground(Color.WHITE);
        this.getContentPane().add(rec);
        JLabel bg = new JLabel(new ImageIcon("src/images/1.jpg"));
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerBtn) {
            String username = usernameText.getText();
            String password = new String(passwordText.getPassword());
            String passwordTrue = new String(passwordTextTrue.getPassword());
            ArrayList<User> user = User.getUserList();
            for (User data : user) {
                System.out.println(data.getUsername());
            }
            boolean found;
            found = (!username.isEmpty() && !password.isEmpty() && !passwordTrue.isEmpty() && password.equals(passwordTrue));
            System.out.println(found);
            if (found) {
                boolean temp = User.readUser(username, user);//检测用户名是否重复
                if (temp) {
                    JOptionPane.showMessageDialog(this, "注册成功", "Success", JOptionPane.INFORMATION_MESSAGE);
                    int id = user.toArray().length;
                    user.add(new User(id, username, password));
                    User.writeUser(user);//将新用户的数据写入json表中
                    this.dispose();
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
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
