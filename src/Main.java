import com.formdev.flatlaf.FlatLightLaf;
import view.login.LoginFrame;
import view.login.User;
import view.music.Sound;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        User.initialUsers();
        FlatLightLaf.setup();
        Sound sound = new Sound("resources/misc/EnterHallownest.wav");
        sound.setVolume(0.5);
        sound.setLooping(true);
        sound.play();
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(sound);
            loginFrame.setVisible(true);
        });
    }
}
