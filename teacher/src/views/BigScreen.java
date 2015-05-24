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
        super("LabSpy - BigScreen");
        this.client = client;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);
        this.setVisible(true);
        this.setSize(800, 600);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i=0; i< e.getClickCount(); i++) {
            this.client.sendMessage(new MouseClickMessage(e.getButton()));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.client.sendMessage(new MousePressMessage(e.getButton()));

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.client.sendMessage(new MouseReleaseMessage(e.getButton()));

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
        client.sendMessage(new StartScreenshot(new Rectangle(this.getWidth(), this.getHeight())));
        while (!this.stopped) {
            if (this.getWidth() != width || this.getHeight() != height) {
                client.sendMessage(new StartScreenshot(new Rectangle(this.getWidth(), this.getHeight())));
                width = this.getWidth();
                height = this.getHeight();
            }

            if (!this.client.isRunning()) {
                this.dispose();
            }
            t = (t++) % 10;
            if (t == 0) {
                this.client.sendMessage(position);
            }
            if (this.isActive()) {
                this.repaint();
            }
            try {
                Thread.sleep(1000/20);
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
        super.paint(g); // paintComponent redesenha o painel
        System.out.println("Calling repaint");
        Image img = this.client.getLastScreenshot().getImage().getImage();
        if (img == null) {
            return;
        }
        g.drawImage(img, 0, 0, null);
    }
}
