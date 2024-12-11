package view.music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class Sound {
    private static int index = 7;
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private final AtomicLong currentFrame = new AtomicLong();  // 当前帧位置
    private AudioFormat audioFormat;
    private String musicPath;  // 当前音频文件路径
    private volatile boolean isPlaying = false;  // 是否正在播放
    private volatile boolean isLooping = false;  // 是否循环播放
    private Thread playThread;  // 播放线程
    private AudioInputStream audioStream;
    private SourceDataLine sourceDataLine;
    private FloatControl volumeControl;  // 音量控制器
    private long clipLength;  // 音频总时长（帧数）
    private Timer progressTimer;  // 进度条定时器

    public Sound(String musicPath) {
        this.musicPath = musicPath;
        prefetch();
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        Sound.index = index;
    }

    private void prefetch() {
        try {
            audioStream = AudioSystem.getAudioInputStream(new File(musicPath));
            audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(audioFormat);
            clipLength = audioStream.getFrameLength();
            currentFrame.set(0);
            initVolumeControl();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            log.info(e.getMessage());
        }
    }

    private void initVolumeControl() {
        if (sourceDataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            volumeControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
        }
    }

    public void play() {
        if (isPlaying) {
            return;
        }
        isPlaying = true;
        playThread = new Thread(this::playAudio);
        playThread.start();
        startProgressBar();
    }

    private void playAudio() {
        try {
            do {
                sourceDataLine.start();
                audioStream = AudioSystem.getAudioInputStream(new File(musicPath));
                skipToCurrentFrame();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1 && isPlaying) {
                    sourceDataLine.write(buffer, 0, bytesRead);
                    currentFrame.addAndGet(bytesRead / audioFormat.getFrameSize());
                }
                if (!isPlaying) return;
                currentFrame.set(0);
            } while (isLooping && isPlaying);
            stop();
        } catch (IOException | UnsupportedAudioFileException e) {
            log.info(e.getMessage());
        }
    }

    private void skipToCurrentFrame() throws IOException {
        if (currentFrame.get() > 0) {
            long bytesToSkip = currentFrame.get() * audioFormat.getFrameSize();
            long bytesSkipped = audioStream.skip(bytesToSkip);
            if (bytesSkipped != bytesToSkip) {
                log.warning("跳过 " + bytesSkipped + " 字节, expected " + bytesToSkip);
            }
        }
    }

    public void pause() {
        if (!isPlaying) {
            return;
        }
        isPlaying = false;
        sourceDataLine.stop();
        stopProgressBar();
    }

    public void stop() {
        isPlaying = false;
        currentFrame.set(0);
        if (sourceDataLine != null) {
            sourceDataLine.stop();
            sourceDataLine.close();
        }
        if (playThread != null && playThread.isAlive()) {
            try {
                playThread.join();
            } catch (InterruptedException e) {
                log.info(e.getMessage());
            }
        }
        stopProgressBar();
        prefetch();
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
        System.out.println("循环已" + (looping ? "开启喵" : "关闭喵"));
    }

    public void changeSource(String newPath) {
        stop();
        this.musicPath = newPath;
        prefetch();
        System.out.println("\n改变音频路径: " + newPath + "喵");
    }

    public String getMusicPath() {
        return musicPath;
    }

    public double getVolume() {
        if (volumeControl == null) {
            System.err.println("声音控制不支持喵！");
            return 0.0;
        }
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        float mid = (max + min) / 2;
        return (volumeControl.getValue() - mid) / (max - mid);
    }

    public void setVolume(double volume) {
        if (volumeControl == null) {
            System.err.println("声音控制不支持喵！");
            return;
        }
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        float mid = (max + min) / 2;
        float newVolume = (float) (mid + (max - mid) * volume);
        volumeControl.setValue(newVolume);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private void startProgressBar() {
        progressTimer = new Timer(true);
        progressTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isPlaying) return;
                long currentSeconds = (long) (currentFrame.get() / audioFormat.getFrameRate());
                long totalSeconds = (long) (clipLength / audioFormat.getFrameRate());
                int barLength = 50; // 进度条长度
                double progress = (double) currentFrame.get() / clipLength;
                int filledLength = (int) (barLength * progress);
                StringBuilder bar = new StringBuilder();
                for (int i = 0; i < barLength; i++) {
                    bar.append(i < filledLength ? "=" : "-");
                }
                System.out.printf("\r[%s] %02d:%02d/%02d:%02d", bar,
                        currentSeconds / 60, currentSeconds % 60,
                        totalSeconds / 60, totalSeconds % 60);
            }
        }, 0, 500); // 每 500 毫秒更新一次
    }

    private void stopProgressBar() {
        if (progressTimer != null) {
            progressTimer.cancel();
        }
    }
}
