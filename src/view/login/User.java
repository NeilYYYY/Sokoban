package view.login;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

public class User {
    private static final Logger log = Logger.getLogger(User.class.getName());
    private static final String PATH = "users.json";
    private final int id;
    private String username;
    private String password;
    private final boolean[][] lv;

    public User(int id, String username, String password, boolean @NotNull [] @NotNull [] lv) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lv = Arrays.stream(lv).map(boolean[]::clone).toArray(boolean[][]::new);
    }

    public static boolean checkUsername(String username, @NotNull List<User> users) {
        return users.stream().noneMatch(user -> user.getUsername().equals(username));
    }

    public static boolean checkUserPassword(String username, String password) {
        if (isInvalidJson(User.PATH)) {
            initialUsers(true);
        }
        List<User> users = readUsersFromFile();
        return users.stream().anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }

    public static @Nullable User getUser(String username, @NotNull List<User> users) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
    }

    public static @NotNull String getSHA(@NotNull String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA3-512");
        byte[] shaBytes = md.digest(str.getBytes());
        return bytesToHexString(shaBytes);
    }

    public static @NotNull String bytesToHexString(byte @NotNull [] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
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

    public static ArrayList<User> getUserList() {
        if (isInvalidJson(User.PATH)) {
            initialUsers(true);
        }
        return readUsersFromFile();
    }

    public static void writeUser(@NotNull List<User> users) {
        removeTrailingDeletedUsers(users);
        try (Writer writer = new FileWriter(User.PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonWriter jsonWriter = new JsonWriter(writer);
            jsonWriter.setIndent("    ");
            gson.toJson(users, ArrayList.class, jsonWriter);
            jsonWriter.flush();
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    public static void removeTrailingDeletedUsers(@NotNull List<User> users) {
        ListIterator<User> iterator = users.listIterator(users.size());
        while (iterator.hasPrevious()) {
            if (!iterator.previous().getUsername().equals("DELETED")) {
                break;
            }
            iterator.remove();
        }
    }

    public static void initialUsers(boolean force) {
        File file = new File(User.PATH);
        if (!file.exists() || force) {
            log.info(force ? "强制初始化用户列表..." : "正在初始化用户列表...");
            List<User> defaultUsers = Arrays.asList(
                    new User(0, "", "", createDefaultLv(false)),
                    new User(1, "admin", "64d09d9930c8ecf79e513167a588cb75439b762ce8f9b22ea59765f32aa74ca19d2f1e97dc922a3d4954594a05062917fb24d1f8e72f2ed02a58ed7534f94d27", createDefaultLv(true)),
                    new User(2, "Box", "c964a72641eea046484af0198742a258d814224e6400400777959bb94ab811bf505ef4a5d1eb46f2f909f4b6bfa45b27af07bdfd5943cf3c0f6e65e8dc81430b", createDefaultLv(false))
            );
            writeUser(defaultUsers);
            log.info("初始化成功喵！");
        }
    }

    public static boolean isInvalidJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            return jsonElement == null;
        } catch (JsonSyntaxException | IOException e) {
            log.warning("文件格式不正确喵: " + e.getMessage());
            return true;
        }
    }

    public static boolean @NotNull [] @NotNull [] createDefaultLv(boolean value) {
        boolean[][] lv = new boolean[3][6];
        for (boolean[] booleans : lv) {
            Arrays.fill(booleans, value);
        }
        return lv;
    }

    private static ArrayList<User> readUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(User.PATH))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line).append("\n");
            }
            Gson gson = new Gson();
            return gson.fromJson(json.toString(), new TypeToken<ArrayList<User>>() {
            }.getType());
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return new ArrayList<>();
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
}