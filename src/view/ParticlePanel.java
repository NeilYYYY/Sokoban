package view;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ParticlePanel extends JPanel {
    private final ArrayList<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private final int particleCount; // 粒子数量
    private final boolean mode;
    private final boolean style;

    public ParticlePanel(int particleCount, boolean mode, boolean style) {
        this.particleCount = particleCount;
        this.mode = mode;
        this.style = style;
        // 定时器更新粒子位置
        // 每16ms更新一次（约60FPS）
        Timer timer = new Timer(16, _ -> {
            if (particles.isEmpty() && getWidth() > 0 && getHeight() > 0) {
                // 初始化粒子
                for (int i = 0; i < this.particleCount; i++) {
                    particles.add(createParticle());
                }
            }
            for (Particle p : particles) {
                p.move();
            }
            repaint();
        });
        timer.start();
    }

    @Contract(" -> new")
    private @NotNull Particle createParticle() {
        int x = random.nextInt(getWidth());
        int y = random.nextInt(getHeight());
        int size = random.nextInt(3) + 1;
        int dx = random.nextInt(3) - 1;
        int dy = random.nextInt(3) - 1;
        while (dx == 0 && dy == 0) { // 确保粒子不静止
            dx = random.nextInt(3) - 1;
            dy = random.nextInt(3) - 1;
        }
        float alpha = random.nextFloat(); // 随机初始透明度
        return new Particle(x, y, size, dx, dy, alpha);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // 抗锯齿设置
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制粒子
        for (Particle p : particles) {
            drawGlowingParticle(g2d, p.x, p.y, p.size, p.alpha);
        }
    }

    private void drawGlowingParticle(@NotNull Graphics2D g2d, int x, int y, int size, float alpha) {
        // 创建白金光的渐变
        int radius = size * 2;
        float[] dist = {0f, 0.5f, 1f};
        Color[] colors = getColors(alpha);
        RadialGradientPaint gradient = new RadialGradientPaint(
                new Point(x, y), radius, dist, colors,
                MultipleGradientPaint.CycleMethod.NO_CYCLE
        );

        // 设置渐变
        g2d.setPaint(gradient);
        g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    private Color @NotNull [] getColors(float alpha) {
        Color[] colors;
        if (style) {
            if (mode) {
                colors = new Color[]{
                        new Color(150, 50, 50, (int) (255 * alpha)),
                        new Color(200, 100, 100, (int) (128 * alpha)),
                        new Color(255, 125, 125, 0),
                };
            } else {
                colors = new Color[]{
                        new Color(255, 255, 225, (int) (255 * alpha)),
                        new Color(255, 225, 225, (int) (128 * alpha)),
                        new Color(255, 225, 225, 0)
                };
            }
        } else {
            colors = new Color[]{
                    new Color(255, 255, 224, (int) (255 * alpha)), // 中心白黄色，动态透明
                    new Color(255, 223, 128, (int) (128 * alpha)), // 渐变为金色，动态透明
                    new Color(255, 223, 128, 0) // 最外层透明
            };
        }
        return colors;
    }

    // 粒子类
    class Particle {
        int x, y; // 粒子位置
        int size; // 粒子大小
        int dx, dy; // 粒子移动方向和速度
        float alpha; // 透明度（0-1）
        boolean fadingOut; // 是否正在淡出

        public Particle(int x, int y, int size, int dx, int dy, float alpha) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.dx = dx;
            this.dy = dy;
            this.alpha = alpha;
            this.fadingOut = true; // 初始为淡出
        }

        public void move() {
            x += dx;
            y += dy;
            // 粒子超出边界时循环回到屏幕内
            if (x < 0 || x > getWidth() || y < 0 || y > getHeight()) {
                reset();
            }

            // 透明度变化
            if (fadingOut) {
                if (style) {
                    alpha -= 0.01f;
                } else {
                    alpha -= 0.02f;
                }
                if (alpha <= 0) {
                    alpha = 0;
                    fadingOut = false; // 开始淡入
                }
            } else {
                if (style) {
                    alpha += 0.01f;
                } else {
                    alpha += 0.02f;
                }
                if (alpha >= 1) {
                    alpha = 1;
                    fadingOut = true; // 开始淡出
                }
            }
        }

        public void reset() {
            x = random.nextInt(getWidth());
            y = random.nextInt(getHeight());
            dx = random.nextInt(3) - 1;
            dy = random.nextInt(3) - 1;
            while (dx == 0 && dy == 0) { // 确保不会生成静止粒子
                dx = random.nextInt(3) - 1;
                dy = random.nextInt(3) - 1;
            }
            size = random.nextInt(3) + 3; // 大小 3-5
            alpha = random.nextFloat(); // 初始透明度随机
            fadingOut = random.nextBoolean(); // 随机开始淡入或淡出
        }
    }
}

