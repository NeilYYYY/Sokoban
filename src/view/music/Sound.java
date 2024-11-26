package view.music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Sound {
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private AudioFormat audioFormat;
    private String musicPath;  // 当前音频文件路径
    private volatile boolean isPlaying = false;  // 是否正在播放
    private volatile boolean isLooping = false;  // 是否循环播放
    private Thread playThread;  // 播放线程
    private AudioInputStream audioStream;
    private SourceDataLine sourceDataLine;
    private FloatControl volumeControl;  // 音量控制器
    private long clipLength;  // 音频总时长（帧数）
    private volatile long currentFrame;  // 当前帧位置

    public Sound(String musicPath) {
        this.musicPath = musicPath;
        prefetch();
    }

    // 初始化音频资源
    private void prefetch() {
        try {
            audioStream = AudioSystem.getAudioInputStream(new File(musicPath));
            audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(audioFormat);
            clipLength = audioStream.getFrameLength();
            currentFrame = 0;

            // 初始化音量控制器
            if (sourceDataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            log.info(e.getMessage());
        }
    }

    // 播放音频
    public void play() {
        if (isPlaying) {
            System.out.println("Audio is already playing.");
            return;
        }
        isPlaying = true;

        playThread = new Thread(() -> {
            try {
                do {
                    sourceDataLine.start();
                    audioStream = AudioSystem.getAudioInputStream(new File(musicPath));
                    if (currentFrame > 0) {
                        long bytesToSkip = currentFrame * audioFormat.getFrameSize();
                        audioStream.skip(bytesToSkip);
                    }
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1 && isPlaying) {
                        sourceDataLine.write(buffer, 0, bytesRead);
                        currentFrame += bytesRead / audioFormat.getFrameSize();
                    }
                    if (!isPlaying) {
                        return; // 中途暂停
                    }
                    currentFrame = 0; // 如果不是暂停，则重置播放位置
                } while (isLooping && isPlaying);

                stop(); // 播放结束时清理资源
            } catch (IOException | UnsupportedAudioFileException e) {
                log.info(e.getMessage());
            }
        });
        playThread.start();
    }

    // 暂停音频
    public void pause() {
        if (!isPlaying) {
            System.out.println("Audio is not currently playing.");
            return;
        }
        isPlaying = false;
        sourceDataLine.stop();
    }

    // 停止音频并重置
    public void stop() {
        isPlaying = false;
        currentFrame = 0; // 停止时重置帧位置
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
        prefetch(); // 重新加载资源
    }

    // 设置循环播放
    public void setLooping(boolean looping) {
        isLooping = looping;
        System.out.println("Looping is set to: " + (looping ? "enabled" : "disabled"));
    }

    // 更换音频文件
    public void changeSource(String newPath) {
        stop(); // 停止当前播放
        this.musicPath = newPath;
        prefetch();
        System.out.println("Changed audio source to: " + newPath);
    }

    // 获取当前播放进度
    public double getProgress() {
        if (clipLength == 0) return 0.0;
        return (double) currentFrame / clipLength * 100.0;
    }

    // 获取当前音量（0.0 ~ 1.0）
    public double getVolume() {
        if (volumeControl == null) {
            System.out.println("Volume control not supported.");
            return 0.0;
        }
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        float mid = (max + min) / 2;
        return (volumeControl.getValue() - mid) / (max - mid);
    }

    // 设置音量（0.0 ~ 1.0）
    public void setVolume(double volume) {
        if (volumeControl == null) {
            System.out.println("Volume control not supported.");
            return;
        }
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        float mid = (max + min) / 2;
        float newVolume = (float) (mid + (max - mid) * volume);
        volumeControl.setValue(newVolume);
    }

    // 显示播放信息
    public void displayStatus() {
        String status = isPlaying ? "Playing" : "Paused";
        System.out.printf("Status: %s, Progress: %.2f%%, Volume: %.2f%%, Looping: %s\n", status, getProgress(), getVolume() * 100, isLooping ? "Enabled" : "Disabled");
    }

    // 检查是否正在播放
    public boolean isPlaying() {
        return isPlaying;
    }
}
