package threads;

import messages.Screenshot;
import messages.ScreenshotRequest;

import java.awt.*;

/*!
 * Classe responsavel pela captura de tela em um computador
 * cliente conectado a um servidor, tal captura pode ser enviada
 * ao servidor
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
