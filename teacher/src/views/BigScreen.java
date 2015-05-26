package views;

import communication.ClientThread;
import messages.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by fernando on 24/05/15.
 */
public class BigScreen extends JFrame implements MouseListener, MouseWheelListener, MouseMotionListener, KeyListener, Runnable {
    private ClientThread client;
    private boolean stopped;
    private MouseMoveMessage position;

    public BigScreen(ClientThread client) {
        super("LabSpy - BigScreen - "+client.getComputer().getIp());
        this.client = client;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addKeyListener(this);
        this.setVisible(true);
        this.setSize(800, 600);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int button;
        switch(e.getButton()) {
            default:
            case MouseEvent.BUTTON1:
                button = InputEvent.BUTTON1_MASK;
                break;
            case MouseEvent.BUTTON2:
                button = InputEvent.BUTTON2_MASK;
                break;
            case MouseEvent.BUTTON3:
                button = InputEvent.BUTTON3_MASK;
                break;
        }
        this.client.sendMessage(new MousePressMessage(button));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int button;
        switch(e.getButton()) {
            default:
            case MouseEvent.BUTTON1:
                button = InputEvent.BUTTON1_MASK;
                break;
            case MouseEvent.BUTTON2:
                button = InputEvent.BUTTON2_MASK;
                break;
            case MouseEvent.BUTTON3:
                button = InputEvent.BUTTON3_MASK;
                break;
        }
        this.client.sendMessage(new MouseReleaseMessage(button));

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.client.sendMessage(new MouseWheelMessage(e.getWheelRotation()));

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.client.sendMessage(new KeyPressMessage(e.getKeyCode()));

    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.client.sendMessage(new KeyReleaseMessage(e.getKeyCode()));
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.position = new MouseMoveMessage(e.getX(), e.getY(), this.getWidth(), this.getHeight());

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
        client.sendMessage(new ResizeScreenshot(new Rectangle(this.getWidth(), this.getHeight())));
        while (!this.stopped) {
            t = (t++) % 40;
            if (t == 0 && (this.getWidth() != width || this.getHeight() != height)) {
                client.sendMessage(new ResizeScreenshot(new Rectangle(this.getWidth(), this.getHeight())));
                width = this.getWidth();
                height = this.getHeight();
            }

            if (!this.client.isRunning()) {
                this.dispose();
            }
            if (this.isActive()) {
                if (t == 0 && (this.position.getX() != x || this.position.getY() != y)) {
                    this.client.sendMessage(this.position);
                    x = this.position.getX();
                    y = this.position.getY();
                }
                this.repaint();
            }
            try {
                Thread.sleep(1000/30);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        this.stopped = true;
    }

    @Override
    public void paint(Graphics g) {
        Image img = this.client.getLastScreenshot().getImage().getImage();
        if (img == null) {
            return;
        }
        g.drawImage(img, 0, 0, null);
    }
}
