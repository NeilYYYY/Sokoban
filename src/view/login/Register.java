package view.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import view.level.LevelFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class Register extends JFrame implements ActionListener {

    private JTextField usernameText;
    private JPasswordField passwordText;
    private JPasswordField passwordTextTrue;
    private JButton registerBtn;

    public Register() {
        setLayout(null);
        setTitle("Register");
        setSize(350, 250);
        setAlwaysOnTop(true);//设置界面一直处于最上层
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //组件
        JLabel username = new JLabel("Username:");
        JLabel password = new JLabel("Password:");
        JLabel passwordTrue = new JLabel("Confirm Password:");
        registerBtn = new JButton("Register");
        registerBtn.addActionListener(this);
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
        JPanel passwordJp = new JPanel();
        JPanel passwordTrueJp = new JPanel();
        JPanel registerJp = new JPanel();
        //设置容器的位置
        usernameJp.setBounds(30, 0, 300, 40);
        passwordJp.setBounds(30, 50, 300, 40);
        passwordTrueJp.setBounds(18, 100, 300, 40);
        registerJp.setBounds(20, 140, 300, 50);

        usernameJp.add(username);
        usernameJp.add(usernameText);
        passwordJp.add(password);
        passwordJp.add(passwordText);
        passwordTrueJp.add(passwordTrue);
        passwordTrueJp.add(passwordTextTrue);
        registerJp.add(registerBtn);
        //将组件装入GUI
        add(usernameJp);
        add(passwordJp);
        add(passwordTrueJp);
        add(registerJp);
        setVisible(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameText.getText();
        String password = new String(passwordText.getPassword());
        String passwordTrue = new String(passwordTextTrue.getPassword());
        ArrayList<User> user = getUserList();
        for (User data : user) {
            System.out.println(data.getUsername());
        }
        boolean found;
        found = (!username.isEmpty() && !password.isEmpty() && !passwordTrue.isEmpty() && password.equals(passwordTrue));
        System.out.println(found);
        if (found) {
            boolean temp = readUser(username, user);//检测用户名是否重复
            if (temp) {
                JOptionPane.showMessageDialog(this, "注册成功", "Success", JOptionPane.INFORMATION_MESSAGE);
                String id = "00" + (user.toArray().length);
                user.add(new User(id, username, password));
                writeUser(username, password, user);//将新用户的数据写入json表中
                this.dispose();
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                LevelFrame levelFrame = new LevelFrame(510, 200);
                levelFrame.setVisible(false);
                loginFrame.setLevelFrame(levelFrame);
            } else {
                JOptionPane.showMessageDialog(this, "用户名重复，注册失败", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "注册失败,请正确填写用户名和密码", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //获取用户的所有信息数据
    public ArrayList<User> getUserList() {
        try (BufferedReader br = new BufferedReader(new FileReader("src\\users.json"))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line).append("\n");
            }
            Gson gson = new Gson();
            return gson.fromJson(json.toString(), new TypeToken<ArrayList<User>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    //读取用户数据 检查有没有出现用户名重复的情况
    public boolean readUser(String username, ArrayList<User> user) {
        for (User data : user) {
            if (data.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    //将用户信息写入json文件中
    public void writeUser(String username, String password, ArrayList<User> user) {
        try (Writer writer = new FileWriter("src\\users.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(user, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
