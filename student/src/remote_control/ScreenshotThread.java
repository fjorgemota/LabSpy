package remote_control;

import communication.BaseClientThread;
import messages.Screenshot;
import messages.ScreenshotRequest;
import org.w3c.dom.css.Rect;

import java.awt.*;

/*!
 * Classe responsavel pela captura de tela em um computador
 * cliente conectado a um servidor, tal captura pode ser enviada
 * ao servidor
 */
public class ScreenshotThread implements Runnable {
    private BaseClientThread client;
    private RobotThread robot;
    private Rectangle rect;
    private boolean run;

    public ScreenshotThread(BaseClientThread client, RobotThread robot, Rectangle rect) {
        this.client = client;
        this.robot = robot;
        this.rect = rect;
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
            screenshot.getImage().setImage(screenshot.getImage().getImage().getScaledInstance(
                    (int) this.rect.getWidth(),
                    (int) this.rect.getHeight(),
                    Image.SCALE_FAST
            ));
            this.client.sendMessage(screenshot);
            try {
                Thread.sleep(1000/40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
