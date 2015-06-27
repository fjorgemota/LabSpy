package views;

import communication.ClientThread;
import messages.*;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

/**
 * Created by fernando on 24/05/15.
 */
public class BigScreen extends JFrame implements Runnable {
    private ClientThread client;
    private boolean stopped;
    private MouseMoveMessage position;
    private int frames;
    static final String SEND = "SEND";
    static final String POWERDOWN = "POWERDOWN";
    static final String POWERUP = "POWERUP";
    private BigScreenPanel panel;

    public BigScreen(ClientThread client, int fps) {
        super("LabSpy - BigScreen - "+client.getComputer().getIp());
        this.frames = fps;
        this.client = client;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setSize(800, 600);
        panel = new BigScreenPanel(client);
        this.setContentPane(panel);
    }

    public void stop() {
        this.stopped = true;
    }

    @Override
    public void run() {
        this.stopped = false;
        int width = 800;
        int height = 600;
        int x = 0;
        int y = 0;
        int t = 0;
        this.position = new MouseMoveMessage(x, y, width, height);

        this.setSize(width, height);
        this.setIgnoreRepaint(false);
        client.sendMessage(new ResizeScreenshot(new Rectangle(width, height)));
        while (!this.stopped) {
            t = (t++) % this.frames;
            if (t == 0 && (panel.getWidth() != width || panel.getHeight() != height)) {
                client.sendMessage(new ResizeScreenshot(new Rectangle(panel.getWidth(), panel.getHeight())));
                width = panel.getWidth();
                height = panel.getHeight();
            }

            if (!this.client.isRunning()) {
                this.dispose();
                break;
            }
            if (this.isActive()) {
                MouseMoveMessage p = panel.getPosition();
                if (p != null) {
                    this.position = p;
                }
                if (t == 0 && (this.position.getX() != x || this.position.getY() != y)) {
                    this.client.sendMessage(this.position);
                    x = this.position.getX();
                    y = this.position.getY();
                }
                this.repaint();
            }
            try {
                Thread.sleep(/*1000/30*/frames);
            } catch (InterruptedException e) {
                continue;
            }
        }
        client.sendMessage(new ResizeScreenshot(new Rectangle(400, 300)));
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.stop();
    }
}
