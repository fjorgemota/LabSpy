package remote_control;


import messages.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;

/*!
 * Robo que e responsavel pela sincronizaçao do mouse do 
 * computador servidor na tela do computador do cliente, quando
 * este estiver recebendo a imagem da tela do servidor
 */
public class RobotThread implements Runnable {
    private BlockingQueue<RobotMessage> send;
    private BlockingQueue<Screenshot> screenshots;
    private MouseMoveMessage position;

    public RobotThread() {
        this.send = new LinkedBlockingQueue<>(100);
        this.screenshots = new LinkedBlockingQueue<>(2);
        this.position = null;
    }

    @Override
    public void run() {
        try {
            Robot r = new Robot();
            System.out.println("Executing robot thread..");
            Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            while (true) {
                System.out.println("Tamanho da queue do RobotThread: "+this.send.size());
                RobotMessage msg = null;
                while (msg == null) {
                    try {
                        msg = this.send.poll(1000/20, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        continue;
                    }
                    if (this.position != null) {
                        int x = (int) ((
                                screen.getWidth() / ((double) this.position.getWidth())
                        ) * this.position.getX());
                        int y = (int) ((
                                screen.getHeight() / ((double) this.position.getHeight()))
                                * this.position.getY());
                        r.mouseMove(x, y);
                        r.waitForIdle();
                        this.position = null;
                    }
                }

                try {
                    if (msg instanceof KeyPressMessage) {
                        r.keyPress(((KeyPressMessage) msg).getKeyCode());
                    } else if (msg instanceof KeyReleaseMessage) {
                        r.keyRelease(((KeyReleaseMessage) msg).getKeyCode());
                    } else if (msg instanceof MousePressMessage) {
                        MousePressMessage press = (MousePressMessage) msg;
                        r.mousePress(press.getButton());
                    } else if (msg instanceof MouseReleaseMessage) {
                        MouseReleaseMessage release = (MouseReleaseMessage) msg;
                        r.mouseRelease(release.getButton());
                    } else if (msg instanceof MouseWheelMessage) {
                        MouseWheelMessage wheel = (MouseWheelMessage) msg;
                        r.mouseWheel(wheel.getWheel());
                    } else if (msg instanceof ScreenshotRequest) {
                        ScreenshotRequest screenshotRequest = (ScreenshotRequest) msg;
                        BufferedImage buf = r.createScreenCapture(screenshotRequest.getRect());
                        screenshots.offer(new Screenshot(buf));
                    }
                } catch(IllegalArgumentException e2) {
                    e2.printStackTrace();
                }
                r.waitForIdle();
            }
        } catch(AWTException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(RobotMessage msg) {
        while (true) {
            try {
                if (msg instanceof MouseMoveMessage) {
                    this.position = (MouseMoveMessage) msg;
                } else {
                    this.send.put(msg);
                }
            } catch (InterruptedException e) {
                continue;
            }
            break;
        }
    }

    public Screenshot getLastScreenshot() {
        Screenshot result = null;
        System.out.println("Tamanho da queue de screenshots: "+this.send.size());
        while (result == null) {
            try {
                result = this.screenshots.take();
            }
            catch (InterruptedException e){
                continue;
            }
        }
        return result;
    }
}
