import others.Config;

import javax.swing.*;

/**
 * Created by paladini on 4/12/15.
 */
public class Setup {
    public static void main(String[] args) {

        // TODO: Create a GUI to choose the address of teacher computer.
        String input = "";
        while(input.isEmpty()) {
            input = JOptionPane.showInputDialog("What's the hostname or IP from the teacher computer?");
        }


        Config config = Config.getInstance();
        config.setAddress(input);

    }
}
