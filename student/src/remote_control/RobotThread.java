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
        boolean ready = false;

        // Waiting for XServer start on the student machine.
        while (!ready) {
            try {
                Robot r = new Robot();
                ready = true;

                Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                while (true) {
                    RobotMessage msg = null;
                    try {
                        msg = this.send.take();
                    } catch (InterruptedException e) {
                        continue;
                    }
                    synchronized (this) {
                        if (this.position != null) {
                            int x = (int) ((
                                    screen.getWidth() / ((double) this.position.getWidth())
                            ) * this.position.getX());
                            int y = (int) ((
                                    screen.getHeight() / ((double) this.position.getHeight()))
                                    * this.position.getY());
                            r.mouseMove(x, y);
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
            } catch (AWTException | AWTError | NoClassDefFoundError e ) {
                e.printStackTrace();
                System.out.println("XServer not ready yet. Will try later.");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
//        Robot r = null;
//        try {
//            r = new Robot();
//        } catch (AWTException e) {
//            e.printStackTrace();
//        }


    }

    public synchronized void sendMessage(RobotMessage msg) {
        while (true) {
            try {
                synchronized (this) {
                    if (msg instanceof MouseMoveMessage) {
                        this.position = (MouseMoveMessage) msg;
                    }
                }
                this.send.put(msg);
            } catch (InterruptedException e) {
                continue;
            }
            break;
        }
    }

    public Screenshot getLastScreenshot() {
        Screenshot result = null;
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
