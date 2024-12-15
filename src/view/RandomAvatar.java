package view;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class RandomAvatar {
    private static final String API_URL = "https://www.loliapi.com/acg/pp/";
    private static final int IMAGE_WIDTH = 100;
    private static final int IMAGE_HEIGHT = 100;

    // 缓存图片的 Map
    private static final ConcurrentHashMap<String, ImageIcon> imageCache = new ConcurrentHashMap<>();

    public static void preloadImages(int count) {
        new Thread(() -> {
            for (int i = 0; i < count; i++) {
                try {
                    BufferedImage originalImage = loadImageFromApi();
                    if (originalImage != null) {
                        BufferedImage scaledImage = resizeImage(originalImage);
                        ImageIcon imageIcon = new ImageIcon(scaledImage);
                        imageCache.put("image-" + i, imageIcon); // 缓存图片
                        System.out.println("Preloaded image-" + i);
                    }
                } catch (Exception e) {
                    Logger log = Logger.getLogger(RandomAvatar.class.getName());
                    log.info("预加载图片失败喵：" + e.getMessage());
                }
            }
        }).start();
    }

    private static BufferedImage loadImageFromApi() throws Exception {
        URI uri = new URI(RandomAvatar.API_URL);
        HttpURLConnection connection = getHttpURLConnection(uri);

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP response code: " + responseCode);
        }

        try (InputStream inputStream = connection.getInputStream()) {
            return ImageIO.read(inputStream);
        }
    }

    private static @NotNull HttpURLConnection getHttpURLConnection(URI uri) throws IOException {
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        connection.setRequestProperty("Referer", "https://www.loliapi.com/");
        connection.setRequestProperty("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();
        return connection;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        double scale = Math.min((double) RandomAvatar.IMAGE_WIDTH / originalWidth, (double) RandomAvatar.IMAGE_HEIGHT / originalHeight);

        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return resizedImage;
    }

    public static void updateImage(JLabel imageLabel, String key) {
        ImageIcon cachedImage = imageCache.get(key);
        if (cachedImage != null) {
            imageLabel.setText(""); // 清除文字
            imageLabel.setIcon(cachedImage);
        }
    }
}
