package view.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class User {
    private static final Logger log = Logger.getLogger(User.class.getName());
    private final int id;
    private String username;
    private String password;
    private final boolean[][] lv;

    public User(int id, String username, String password, boolean @NotNull [] @NotNull [] lv) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lv = new boolean[lv.length][lv[0].length];
        for (int i = 0; i < lv.length; i++) {
            System.arraycopy(lv[i], 0, this.lv[i], 0, lv[i].length);
        }
    }

    //读取用户数据 检查是否出现用户名重复的情况
    public static boolean readUser(String username, @NotNull ArrayList<User> user) {
        for (User data : user) {
            if (data.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    //读取用户数据 检测用户名密码
    public static boolean checkUser(String username, String password) {
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
            log.info(e.getMessage());
        }
        return false;
    }

    //读取用户数据 找到用户
    public static @Nullable User getUser(String username, @NotNull ArrayList<User> user) {
        for (User data : user) {
            if (data.getUsername().equals(username)) {
                return data;
            }
        }
        return null;
    }

    public static String getSHA(@NotNull String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA3-512");
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

    //获取用户的所有信息数据
    public static ArrayList<User> getUserList() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/users.json"))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line).append("\n");
            }
            Gson gson = new Gson();
            return gson.fromJson(json.toString(), new TypeToken<ArrayList<User>>() {
            }.getType());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return new ArrayList<>();
    }

    //将用户信息写入json文件中
    public static void writeUser(ArrayList<User> user) {
        try (Writer writer = new FileWriter("src/users.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            // 使用 JsonWriter 设置 4 个空格的缩进
            JsonWriter jsonWriter = new JsonWriter(writer);
            jsonWriter.setIndent("    "); // 设置缩进为 4 个空格
            gson.toJson(user, ArrayList.class, jsonWriter);
            jsonWriter.flush();
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean[][] getLv() {
        return lv;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return String.format("%d, %s, %s", id, username, password);
    }
}
