package view.music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
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
    private final AtomicLong currentFrame = new AtomicLong();  // 当前帧位置

    public Sound(String musicPath) {
        this.musicPath = musicPath;
        prefetch();
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
            System.err.println("Audio is already playing.");
            return;
        }
        isPlaying = true;
        playThread = new Thread(this::playAudio);
        playThread.start();
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
                log.warning("Skipped " + bytesSkipped + " bytes, expected " + bytesToSkip);
            }
        }
    }

    public void pause() {
        if (!isPlaying) {
            System.err.println("Audio is not currently playing.");
            return;
        }
        isPlaying = false;
        sourceDataLine.stop();
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
        prefetch();
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
        System.out.println("Looping is set to: " + (looping ? "enabled" : "disabled"));
    }

    public void changeSource(String newPath) {
        stop();
        this.musicPath = newPath;
        prefetch();
        System.out.println("Changed audio source to: " + newPath);
    }

    public String getMusicPath() {
        return musicPath;
    }

    public double getProgress() {
        return clipLength == 0 ? 0.0 : (double) currentFrame.get() / clipLength * 100.0;
    }

    public double getVolume() {
        if (volumeControl == null) {
            System.err.println("Volume control not supported.");
            return 0.0;
        }
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        float mid = (max + min) / 2;
        return (volumeControl.getValue() - mid) / (max - mid);
    }

    public void setVolume(double volume) {
        if (volumeControl == null) {
            System.err.println("Volume control not supported.");
            return;
        }
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        float mid = (max + min) / 2;
        float newVolume = (float) (mid + (max - mid) * volume);
        volumeControl.setValue(newVolume);
    }

    public void displayStatus() {
        String status = isPlaying ? "Playing" : "Paused";
        System.out.printf("Status: %s, Progress: %.2f%%, Volume: %.2f%%, Looping: %s\n", status, getProgress(), getVolume() * 100, isLooping ? "Enabled" : "Disabled");
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}