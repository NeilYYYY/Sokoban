package view;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.*;
import java.security.*;

public class FileSHAUtil {
    public static @NotNull String calculateSHA(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA3-512");
        try (InputStream is = Files.newInputStream(file.toPath()); DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[1024];
            while (dis.read(buffer) != -1) {
                // 读取文件内容并更新SHA
            }
        }
        byte[] digest = md.digest();
        // 将SHA字节数组转换为十六进制字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void saveSHAToFile(String sha, File outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(sha);
        }
    }

    public static String loadSHAFromFile(File inputFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            return reader.readLine();
        }
    }

    @Contract(pure = true, value = "_, null -> false")
    public static boolean compareSHA(@NotNull String shaFromFile, String calculatedSHA) {
        return shaFromFile.equals(calculatedSHA);
    }
}
