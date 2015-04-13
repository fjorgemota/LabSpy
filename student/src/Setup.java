import others.Config;

import javax.swing.*;

/**
 * Created by paladini on 4/12/15.
 */
public class Setup {
    public static void main(String[] args) {

        JOptionPane.showMessageDialog(null, "This setup will guide you installing LabSpy into student computers. You also need run this setup as an administrator (\"sudo\"). \nIf you already have installed LabSpy in this computer, previous configurations will be lost.");

        // TODO: Create a GUI to choose the address of teacher computer.
        String input = "";
        while(input.isEmpty()) {
            input = JOptionPane.showInputDialog("What's the hostname or IP from the teacher computer?");
        }

        Config config = Config.getInstance();
        config.setAddress(input);

        JOptionPane.showMessageDialog(null, "The installation process was finished. Student computer should connect to the teacher's computer in the next reboot.");

    }
}
