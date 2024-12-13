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
import java.util.Arrays;
import java.util.List;
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
        try (BufferedReader br = new BufferedReader(new FileReader("users.json"))) {
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
        try (BufferedReader br = new BufferedReader(new FileReader("users.json"))) {
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
        try (Writer writer = new FileWriter("users.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonWriter jsonWriter = new JsonWriter(writer);
            jsonWriter.setIndent("    "); // 设置缩进为 4 个空格
            gson.toJson(user, ArrayList.class, jsonWriter);
            jsonWriter.flush();
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    public static void initialUsers() {
        String filePath = "users.json";
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("users.json does not exist. Generating...");
            List<User> defaultUsers = Arrays.asList(
                    new User(0, "", "", createDefaultLv(false)),
                    new User(1, "admin", "64d09d9930c8ecf79e513167a588cb75439b762ce8f9b22ea59765f32aa74ca19d2f1e97dc922a3d4954594a05062917fb24d1f8e72f2ed02a58ed7534f94d27", createDefaultLv(true)),
                    new User(2, "Box", "c964a72641eea046484af0198742a258d814224e6400400777959bb94ab811bf505ef4a5d1eb46f2f909f4b6bfa45b27af07bdfd5943cf3c0f6e65e8dc81430b", createDefaultLv(false))
            );

            try (FileWriter writer = new FileWriter(file)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonWriter jsonWriter = new JsonWriter(writer);
                jsonWriter.setIndent("    ");
                gson.toJson(defaultUsers, ArrayList.class, jsonWriter);
                jsonWriter.flush();
                System.out.println("user.json generated successfully.");
            } catch (IOException e) {
                System.err.println("Failed to write user.json: " + e.getMessage());
            }
        }
    }

    public static boolean[][] createDefaultLv(boolean value) {
        boolean[][] lv = new boolean[3][6];
        for (boolean[] booleans : lv) {
            Arrays.fill(booleans, value);
        }
        return lv;
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
