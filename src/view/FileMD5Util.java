package view;

import java.io.*;
import java.nio.file.*;
import java.security.*;

public class FileMD5Util {

    // 计算文件的MD5
    public static String calculateMD5(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(file.toPath()); DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[1024];
            while (dis.read(buffer) != -1) {
                // 读取文件内容并更新MD5
            }
        }
        byte[] digest = md.digest();
        // 将MD5字节数组转换为十六进制字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 保存MD5到文件
    public static void saveMD5ToFile(String md5, File outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(md5);
        }
    }

    // 从文件加载MD5
    public static String loadMD5FromFile(File inputFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            return reader.readLine();
        }
    }

    // 比较两个MD5
    public static boolean compareMD5(String md5FromFile, String calculatedMD5) {
        return md5FromFile.equals(calculatedMD5);
    }
}
