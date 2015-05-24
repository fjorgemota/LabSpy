package remote_control;

import communication.BaseClientThread;
import messages.Screenshot;
import messages.ScreenshotRequest;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;

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
        Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        while(this.run) {
            robot.sendMessage(new ScreenshotRequest(screen));
            Screenshot screenshot = robot.getLastScreenshot();
            Image image = screenshot.getImage().getImage();
            // Create a buffered image with transparency
            BufferedImage bImage = new BufferedImage(
                    (int) screen.getWidth(),
                    (int) screen.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D bGr = bImage.createGraphics();
            bGr.drawImage(image, 0, 0, null);
            Point location = MouseInfo.getPointerInfo().getLocation();
            bGr.setColor(Color.BLUE);
            bGr.translate(0, 0);
            bGr.fillOval((int) location.getX(), (int) location.getY(), 20, 20);
            bGr.dispose();


            screenshot.getImage().setImage(bImage.getScaledInstance(
                    (int) this.rect.getWidth(),
                    (int) this.rect.getHeight(),
                    Image.SCALE_FAST
            ));

            this.client.sendMessage(screenshot);
        }
    }
}
