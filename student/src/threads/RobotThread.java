package threads;


import messages.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by fernando on 11/04/15.
 */
public class RobotThread implements Runnable {
    private Queue<RobotMessage> send;
    private Queue<Screenshot> screenshots;

    public RobotThread() {
        this.send = new ConcurrentLinkedQueue<RobotMessage>();
        this.screenshots = new ConcurrentLinkedQueue<Screenshot>();
    }

    @Override
    public void run() {
        try {
            Robot r = new Robot();
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
                    screenshots.add(new Screenshot(buf));
                }
            }
        } catch(AWTException e) {
            return;
        }
    }

    public void sendMessage(RobotMessage msg) {
        this.send.add(msg);
    }

    public Screenshot getLastScreenshot() {
        while (this.screenshots.isEmpty()) {}
        return this.screenshots.poll();
    }
}
