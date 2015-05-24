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

    public RobotThread() {
        this.send = new LinkedBlockingQueue<>(10000);
        this.screenshots = new LinkedBlockingQueue<>(100);
    }

    @Override
    public void run() {
        try {
            Robot r = new Robot();
            System.out.println("Executing robot thread..");
            Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            r.setAutoWaitForIdle(true);
            while (true) {
                RobotMessage msg = null;
                while (msg == null) {
                    try {
                        msg = this.send.take();
                    } catch (InterruptedException e) {
                        continue;
                    }
                }

                try {
                    if (msg instanceof KeyPressMessage) {
                        r.keyPress(((KeyPressMessage) msg).getKeyCode());
                    } else if (msg instanceof KeyReleaseMessage) {
                        r.keyPress(((KeyReleaseMessage) msg).getKeyCode());
                    } else if (msg instanceof MouseClickMessage) {
                        MouseClickMessage click = (MouseClickMessage) msg;
                        r.mousePress(click.getButton());
                        r.mouseRelease(click.getButton());
                    } else if (msg instanceof MousePressMessage) {
                        MousePressMessage press = (MousePressMessage) msg;
                        r.mousePress(press.getButton());
                    } else if (msg instanceof MouseMoveMessage) {
                        MouseMoveMessage move = (MouseMoveMessage) msg;
                        int x = (int) ((
                                screen.getWidth() / ((double) move.getWidth())
                        ) * move.getX());
                        int y = (int) ((
                                screen.getHeight() / ((double) move.getHeight()))
                                * move.getY());
                        r.mouseMove(x, y);
                    } else if (msg instanceof MouseReleaseMessage) {
                        MouseReleaseMessage release = (MouseReleaseMessage) msg;
                        r.mouseRelease(release.getButton());
                    } else if (msg instanceof MouseWheelMessage) {
                        MouseWheelMessage wheel = (MouseWheelMessage) msg;
                        r.mouseWheel(wheel.getWheel());
                    } else if (msg instanceof ScreenshotRequest) {
                        ScreenshotRequest screenshotRequest = (ScreenshotRequest) msg;
                        BufferedImage buf = r.createScreenCapture(screenshotRequest.getRect());
                        while (true) {
                            try {
                                screenshots.put(new Screenshot(buf));
                            } catch (InterruptedException e3) {
                                continue;
                            }
                            break;
                        }
                    }
                } catch(IllegalArgumentException e2) {
                    continue;
                }
            }
        } catch(AWTException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(RobotMessage msg) {
        while (true) {
            try {
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
