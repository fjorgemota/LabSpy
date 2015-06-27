package remote_control;

import communication.BaseClientThread;
import messages.Screenshot;
import messages.ScreenshotRequest;
import org.imgscalr.Scalr;
import org.w3c.dom.css.Rect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Graphics;
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

        robot.sendMessage(new ScreenshotRequest(screen));

        if (classLoader == null) {
            classLoader = Class.class.getClassLoader();
        }
        BufferedImage cursor = null;
        double fpsAvg = 0;
        while(this.run) {
            robot.sendMessage(new ScreenshotRequest(screen));
            long start = System.currentTimeMillis();
            Screenshot screenshot = robot.getLastScreenshot();

            BufferedImage bImage = (BufferedImage) screenshot.getImage().getImage();
            // Create a buffered image with transparency
            Graphics bGr = bImage.createGraphics();
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
                    bGr.drawOval( (int) location.getX(),  (int) location.getY(), 20, 20);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bGr.dispose();

            bImage = Scalr.resize(
                    bImage,
                    Scalr.Method.SPEED,
                    (int) this.rect.getWidth(),
                    (int) this.rect.getHeight()
            );

            screenshot.getImage().setImage(bImage);

            fpsAvg = ((1000 / (System.currentTimeMillis() - start+1))+fpsAvg)/2;
            System.out.println("FPS: " + Math.round(fpsAvg));

            synchronized (this) {
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
