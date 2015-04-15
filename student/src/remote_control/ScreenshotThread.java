package remote_control;

import communication.BaseClientThread;
import messages.Screenshot;
import messages.ScreenshotRequest;

import java.awt.*;

/*!
 * Classe responsavel pela captura de tela em um computador
 * cliente conectado a um servidor, tal captura pode ser enviada
 * ao servidor
 */
public class ScreenshotThread implements Runnable {
    private BaseClientThread client;
    private RobotThread robot;
    private boolean run;

    public ScreenshotThread(BaseClientThread client, RobotThread robot) {
        this.client = client;
        this.robot = robot;
        this.run = true;
    }

    public void stop() {
        this.run = false;
    }

    public void run() {
        System.out.println("Executing screenshot thread..");
        Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        while(this.run) {
            System.out.println("Putting screenshot request into robot..");
            robot.sendMessage(new ScreenshotRequest(screen));
            Screenshot screenshot = robot.getLastScreenshot();
            this.client.sendMessage(screenshot);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
