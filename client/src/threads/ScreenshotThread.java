package threads;

import messages.Screenshot;
import messages.ScreenshotRequest;

import java.awt.*;

/**
 * Created by fernando on 11/04/15.
 */
public class ScreenshotThread implements Runnable {
    private ClientThread client;
    private RobotThread robot;
    private boolean run;

    public ScreenshotThread(ClientThread client, RobotThread robot) {
        this.client = client;
        this.robot = robot;
    }

    public void stop() {
        this.run = false;
    }

    public void run() {
        Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        while(this.run) {
            robot.sendMessage(new ScreenshotRequest(screen));
            Screenshot screenshot = robot.getLastScreenshot();
            this.client.sendMessage(screenshot);
        }
    }
}
