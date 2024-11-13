package view.game;

import javax.management.InvalidApplicationException;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayerUtil extends Thread {
    private String fileName;
    public static boolean flag = true;
    public SoundPlayerUtil(String wavFile) {
        this.fileName = wavFile;
    }

    public void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fileName));
            AudioFormat audioFormat = audioInputStream.getFormat();
            final SourceDataLine sourceDataLine;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            FloatControl floatControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            double value = 0.1;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            floatControl.setValue(dB);
            int nByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {
                if (flag){
                    nByte = audioInputStream.read(buffer, 0, SIZE);
                    sourceDataLine.write(buffer, 0, nByte);
                }
                else {
                    nByte = audioInputStream.read(buffer, 0, 0);
                }

            }
            sourceDataLine.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopSound() {
        flag = false;
    }
    /*
    private Clip clip;

    public void loadAudioFile(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void play(){
        if(clip != null){
            clip.start();
        }
    }
    public void pause() {
        if(clip != null){
            clip.stop();
        }
    }
    public void stop(){
        if(clip != null){
            clip.stop();
            clip.setFramePosition(0);
        }
    }

     */
}
