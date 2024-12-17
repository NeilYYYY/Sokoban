package view;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class RandomAvatar {
    private static final String API_URL = "https://www.loliapi.com/acg/pp/";
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 100;
    private static final Logger log = Logger.getLogger(RandomAvatar.class.getName());
    private static ImageIcon imageCache = null;

    public static void preloadAvatar() {
        new Thread(() -> {
            try {
                BufferedImage originalImage = loadImageFromApi();
                if (originalImage != null) {
                    BufferedImage scaledImage = resizeImage(originalImage);
                    imageCache = new ImageIcon(scaledImage);
                    System.out.println("预加载图片成功喵！");
                }
            } catch (Exception e) {
                log.info("预加载图片失败喵：" + e.getMessage());
            }
        }).start();
    }

    private static BufferedImage loadImageFromApi() throws Exception {
        URI uri = new URI(API_URL);
        if (!isNetworkAvailable()) {
            throw new IOException("网络不可用，请检查网络连接。");
        }
        HttpURLConnection connection = getHttpURLConnection(uri);
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP response code: " + responseCode);
        }
        try (InputStream inputStream = connection.getInputStream()) {
            return ImageIO.read(inputStream);
        }
    }

    private static boolean isNetworkAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.loliapi.com");
            return address.isReachable(2000);
        } catch (IOException e) {
            return false;
        }
    }

    private static @NotNull HttpURLConnection getHttpURLConnection(@NotNull URI uri) throws IOException {
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        connection.setRequestProperty("Referer", "https://www.loliapi.com/");
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(2000);
        connection.connect();
        return connection;
    }

    private static @NotNull BufferedImage resizeImage(@NotNull BufferedImage originalImage) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        double scale = Math.min((double) IMAGE_WIDTH / originalWidth, (double) IMAGE_HEIGHT / originalHeight);
        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    public static void updateAvatar(JLabel imageLabel) {
        if (imageCache != null) {
            imageLabel.setText("");
            imageLabel.setIcon(imageCache);
        }
    }
}