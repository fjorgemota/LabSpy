package remote_control;


import messages.*;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    private ExecutorService executor;
    private Rectangle screen;

    public RobotThread() {
        this.send = new ArrayBlockingQueue<RobotMessage>(100, true);
        this.screenshots = new ArrayBlockingQueue<Screenshot>(10, true);
        this.position = null;
        this.screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }

    @Override
    public void run() {
        Robot r = null;
        while (true) {
            try {
                if (r == null) {
                    r = new Robot();
                }
                RobotMessage msg;
                msg = this.send.take();
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
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                }
                r.waitForIdle();
            } catch (AWTException e) {
                e.printStackTrace();
            } catch (InterruptedException ignored) {
            }
        }
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
            catch (InterruptedException ignored){
            }
        }
        return result;
    }
}
