import view.login.LoginFrame;
import view.music.Sound;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Sound sound = new Sound("src/misc/EnterHallownest.wav");
        sound.setLooping(true);
        sound.play();
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(sound);
            loginFrame.setVisible(true);
        });
    }
}
