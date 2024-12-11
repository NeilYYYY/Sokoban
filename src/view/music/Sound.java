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
        return Sound.index;
    }

    public static void setIndex(int index) {
        Sound.index = index;
    }

    private void prefetch() {
        try {
            this.audioStream = AudioSystem.getAudioInputStream(new File(this.musicPath));
            this.audioFormat = this.audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, this.audioFormat);
            this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            this.sourceDataLine.open(audioFormat);
            this.clipLength = this.audioStream.getFrameLength();
            this.currentFrame.set(0);
            initVolumeControl();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            log.info(e.getMessage());
        }
    }

    private void initVolumeControl() {
        if (this.sourceDataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            this.volumeControl = (FloatControl) this.sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
        }
    }

    public void play() {
        if (this.isPlaying) {
            return;
        }
        this.isPlaying = true;
        this.playThread = new Thread(this::playAudio);
        this.playThread.start();
        startProgressBar();
    }

    private void playAudio() {
        try {
            do {
                this.sourceDataLine.start();
                this.audioStream = AudioSystem.getAudioInputStream(new File(this.musicPath));
                skipToCurrentFrame();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = this.audioStream.read(buffer, 0, buffer.length)) != -1 && this.isPlaying) {
                    this.sourceDataLine.write(buffer, 0, bytesRead);
                    this.currentFrame.addAndGet(bytesRead / this.audioFormat.getFrameSize());
                }
                if (!this.isPlaying) return;
                this.currentFrame.set(0);
            } while (this.isLooping && this.isPlaying);
            stop();
        } catch (IOException | UnsupportedAudioFileException e) {
            log.info(e.getMessage());
        }
    }

    private void skipToCurrentFrame() throws IOException {
        if (this.currentFrame.get() > 0) {
            long bytesToSkip = this.currentFrame.get() * this.audioFormat.getFrameSize();
            long bytesSkipped = this.audioStream.skip(bytesToSkip);
            if (bytesSkipped != bytesToSkip) {
                log.warning("跳过 " + bytesSkipped + " 字节, expected " + bytesToSkip);
            }
        }
    }

    public void pause() {
        if (!this.isPlaying) {
            return;
        }
        this.isPlaying = false;
        this.sourceDataLine.stop();
        stopProgressBar();
    }

    public void stop() {
        this.isPlaying = false;
        this.currentFrame.set(0);
        if (this.sourceDataLine != null) {
            this.sourceDataLine.stop();
            this.sourceDataLine.close();
        }
        if (playThread != null && playThread.isAlive()) {
            try {
                this.playThread.join();
            } catch (InterruptedException e) {
                log.info(e.getMessage());
            }
        }
        stopProgressBar();
        prefetch();
    }

    public void setLooping(boolean looping) {
        this.isLooping = looping;
        System.out.println("循环已" + (looping ? "开启喵" : "关闭喵"));
    }

    public void changeSource(String newPath) {
        stop();
        this.musicPath = newPath;
        prefetch();
        System.out.println("\n改变音频路径: " + newPath);
    }

    public String getMusicPath() {
        return this.musicPath;
    }

    public double getVolume() {
        if (this.volumeControl == null) {
            System.err.println("声音控制不支持喵！");
            return 0.0;
        }
        float min = this.volumeControl.getMinimum();
        float max = this.volumeControl.getMaximum();
        float mid = (max + min) / 2;
        return (this.volumeControl.getValue() - mid) / (max - mid);
    }

    public void setVolume(double volume) {
        if (this.volumeControl == null) {
            System.err.println("声音控制不支持喵！");
            return;
        }
        float min = this.volumeControl.getMinimum();
        float max = this.volumeControl.getMaximum();
        float mid = (max + min) / 2;
        float newVolume = (float) (mid + (max - mid) * volume);
        this.volumeControl.setValue(newVolume);
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    private void startProgressBar() {
        this.progressTimer = new Timer(true);
        this.progressTimer.scheduleAtFixedRate(new TimerTask() {
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
                System.out.printf("\r[%s] %02d:%02d/%02d:%02d", bar, currentSeconds / 60, currentSeconds % 60, totalSeconds / 60, totalSeconds % 60);
            }
        }, 0, 500); // 每 500 毫秒更新一次
    }

    private void stopProgressBar() {
        if (this.progressTimer != null) {
            this.progressTimer.cancel();
        }
    }

    public long getCurrentFrame() {
        return this.currentFrame.get();
    }

    public long getClipLength() {
        return this.clipLength;
    }

    public float getFrameRate() {
        if (this.audioFormat != null) {
            return this.audioFormat.getFrameRate();
        }
        return -1; // 如果音频格式未初始化，返回一个无效值。
    }
}
