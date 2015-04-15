package remote_control;


import messages.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/*!
 * Robo que e responsavel pela sincronizaçao do mouse do 
 * computador servidor na tela do computador do cliente, quando
 * este estiver recebendo a imagem da tela do servidor
 */
public class RobotThread implements Runnable {
    private BlockingQueue<RobotMessage> send;
    private BlockingQueue<Screenshot> screenshots;

    public RobotThread() {
        this.send = new LinkedBlockingQueue<>();
        this.screenshots = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        try {
            Robot r = new Robot();
            System.out.println("Executing robot thread..");
            while (true) {
                RobotMessage msg = this.send.poll();
                if(msg instanceof MousePressMessage) {
                    MousePressMessage press = (MousePressMessage) msg;
                    r.mousePress(press.getButton());
                } else if(msg instanceof MouseMoveMessage) {
                    MouseMoveMessage move = (MouseMoveMessage) msg;
                    r.mouseMove(move.getX(), move.getY());
                } else if(msg instanceof MouseReleaseMessage) {
                    MouseReleaseMessage release = (MouseReleaseMessage) msg;
                    r.mouseRelease(release.getButton());
                } else if(msg instanceof MouseWheelMessage) {
                    MouseWheelMessage wheel = (MouseWheelMessage) msg;
                    r.mouseWheel(wheel.getWheel());
                } else if(msg instanceof ScreenshotRequest) {
                    ScreenshotRequest screenshotRequest = (ScreenshotRequest) msg;
                    BufferedImage buf = r.createScreenCapture(screenshotRequest.getRect());
                    System.out.println("Saving screenshot..");
                    screenshots.add(new Screenshot(buf));
                }
            }
        } catch(AWTException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(RobotMessage msg) {
        this.send.add(msg);
    }

    public Screenshot getLastScreenshot() {
        System.out.println("Awaiting last screenshot..");
        while (this.screenshots.isEmpty()) {}
        System.out.println("Screenshot received. Returning..");
        return this.screenshots.poll();
    }
}
