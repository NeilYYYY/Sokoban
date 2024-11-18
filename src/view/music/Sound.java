package view.music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private String musicPath;  // 当前音频文件路径
    private volatile boolean isPlaying = false;  // 是否正在播放
    private Thread playThread;  // 播放线程
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceDataLine;
    private long clipLength;  // 音频总时长
    private long currentFrame;  // 当前帧位置

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
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
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
                sourceDataLine.start();
                audioStream = AudioSystem.getAudioInputStream(new File(musicPath));
                if (currentFrame > 0) {
                    audioStream.skip(currentFrame * audioFormat.getFrameSize());
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

                // 播放完毕清理
                stop();
            } catch (IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
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
        currentFrame = 0;
        if (sourceDataLine != null) {
            sourceDataLine.stop();
            sourceDataLine.close();
        }
        if (playThread != null && playThread.isAlive()) {
            try {
                playThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        prefetch(); // 重新加载资源
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

    // 显示播放信息
    public void displayStatus() {
        String status = isPlaying ? "Playing" : "Paused";
        System.out.printf("Status: %s, Progress: %.2f%%\n", status, getProgress());
    }

    // 检查是否正在播放
    public boolean isPlaying() {
        return isPlaying;
    }

    // 获取音频总时长（秒）
    public long getDuration() {
        return clipLength / (long) audioFormat.getFrameRate();
    }
}
