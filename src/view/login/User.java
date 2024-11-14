package view.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;

public class User {
    private final int id;
    private final String username;
    private final String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    //读取用户数据 检查有没有出现用户名重复的情况
    public static boolean readUser(String username, ArrayList<User> user) {
        for (User data : user) {
            if (data.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    //读取用户数据 检查有没有出现用户名重复的情况
    public static User getUser(String username, ArrayList<User> user) {
        for (User data : user) {
            if (data.getUsername().equals(username)) {
                return data;
            }
        }
        return null;
    }

    //获取用户的所有信息数据
    public static ArrayList<User> getUserList() {
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

    //将用户信息写入json文件中
    public static void writeUser(ArrayList<User> user) {
        try (Writer writer = new FileWriter("src\\users.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(user, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%d, %s, %s", id, username, password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
