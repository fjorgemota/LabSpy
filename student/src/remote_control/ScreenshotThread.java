package remote_control;

import communication.BaseClientThread;
import messages.Screenshot;
import messages.ScreenshotRequest;
import org.w3c.dom.css.Rect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.locks.Lock;

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
    private int fps;

    public ScreenshotThread(BaseClientThread client, RobotThread robot, Rectangle rect) {
        this.client = client;
        this.robot = robot;
        this.rect = rect;
        this.fps = 40;
    }

    public synchronized void stop() {
        this.run = false;
    }

    public synchronized void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public synchronized void setFrames(int frames) {
        this.fps = frames;
    }

    @Override
    public void run() {
        this.run = true;

        Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null) {
            classLoader = Class.class.getClassLoader();
        }
        BufferedImage cursor = null;
        while(this.run) {
            synchronized (this) {
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
                bGr.translate(0, 0);
                try {
                    if (cursor == null) {
                        cursor = ImageIO.read(classLoader.getResourceAsStream("imagens/cursor.png"));
                    }
                    if (cursor != null) {
                        bGr.drawImage(cursor, (int) location.getX(), (int) location.getY(), 18, 30, null);
                    } else {
                        bGr.setColor(Color.BLUE);
                        bGr.drawOval( (int) location.getX(), (int) location.getY(), 20, 20);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bGr.dispose();


                screenshot.getImage().setImage(bImage.getScaledInstance(
                        (int) this.rect.getWidth(),
                        (int) this.rect.getHeight(),
                        Image.SCALE_FAST
                ));
                if (this.run) {
                    this.client.sendMessage(screenshot);
                }
            }
            try {
                Thread.sleep(/*1000/30*/fps);
            } catch (InterruptedException e){
                continue;
            }
        }
    }
}
