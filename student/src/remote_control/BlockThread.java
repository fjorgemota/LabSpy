package remote_control;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by fernando on 27/06/15.
 */
public class BlockThread extends JFrame implements Runnable {
    private boolean blocked;
    private boolean stopped;
    private ScreenshotThread screenshotThread;

    public BlockThread(ScreenshotThread screenshotThread) {
        this.blocked = false;
        this.stopped = false;
        this.screenshotThread = screenshotThread;

    }

    public synchronized void block() {
        this.blocked = true;
    }

    public synchronized void unblock() {
        this.blocked = false;
    }

    public void stop(){
        this.stopped = true;
    }

    @Override
    public void run() {
        Random r = new Random();
        this.enableInputMethods(false);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice vc = env.getDefaultScreenDevice();
        this.setVisible(false);
        this.setUndecorated(true);
        this.toBack();
        while (!this.stopped) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                continue;
            }
            synchronized (this) {
                if (!this.blocked) {
                    this.setVisible(false);
                    vc.setFullScreenWindow(null);
                    this.toBack();
                    continue;
                }
            }
            BufferedImage screen = this.screenshotThread.getLastScreenshot();
            if (screen == null) {
                continue;
            }
            int color;
            boolean focus;
            color = screen.getRGB(0, 0);
            focus = color != 0;
            if (!focus) {
                color = screen.getRGB(0, screen.getHeight()-1);
                focus = color != 0;
            }

            color = screen.getRGB(screen.getWidth()-1, 0);
            if (!focus) {
                focus = color != 0;
            }

            if (!focus) {
                color = screen.getRGB(screen.getWidth()-1, screen.getHeight()-1);
                focus = color != 0;
            }

            for (int i=0; i < 20; i++) {
                int x = r.nextInt(screen.getWidth());
                int y = r.nextInt(screen.getHeight());
                color = screen.getRGB(x, y);
                focus = color != 0;
                if (focus) {
                    break;
                }
            }
            if (focus) {
                this.setExtendedState(JFrame.MAXIMIZED_BOTH);
                this.setVisible(true);
                this.requestFocusInWindow();
                this.toFront();
                vc.setFullScreenWindow(this);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}
