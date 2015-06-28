package views;

import communication.ClientThread;
import messages.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Created by fernando on 24/05/15.
 */
public class BigScreenPanel extends JPanel implements MouseListener, MouseWheelListener, MouseMotionListener, KeyListener {
    private ClientThread client;
    private MouseMoveMessage position;

    public BigScreenPanel(ClientThread client) {
        this.client = client;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);
        this.setVisible(true);
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

    public MouseMoveMessage getPosition()
    {
        return this.position;
    }


    @Override
    public void paint(Graphics g) {
        BufferedImage img = this.client.getLastScreenshot().getImage();
        if (img == null) {
            return;
        }
        g.drawImage(img, 0, 0, null);
    }
}
