import view.login.LoginFrame;
import view.music.Sound;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Sound sound = new Sound("src/misc/東方紅魔郷魔法少女達の百年祭.mid");
        sound.start(true);
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(sound);
            loginFrame.setVisible(true);
        });
    }
}
