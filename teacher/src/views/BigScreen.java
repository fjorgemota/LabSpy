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
public class BigScreen extends JFrame implements Runnable, ActionListener {
    private ClientThread client;
    private boolean stopped;
    private MouseMoveMessage position;
    private int frames;
    static final String SEND = "SEND";
    static final String POWERDOWN = "POWERDOWN";
    static final String POWERUP = "POWERUP";
    static final String FRAMES = "FRAMES";
    static final String BLOCK = "BLOCK";
    static final String UNBLOCK = "UNBLOCK";
    static final String CLIENT = "CLIENT";

    private BigScreenPanel panel;

    public BigScreen(ClientThread client, int fps) {
        super("LabSpy - BigScreen - "+client.getComputer().getIp());
        this.frames = fps;
        this.client = client;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setSize(800, 600);
        panel = new BigScreenPanel(client);

        MenuBar menu = new MenuBar();
        Menu actions = new Menu("Actions");
        MenuItem block = new MenuItem("Block");
        block.addActionListener(this);
        block.setActionCommand(BLOCK);
        actions.add(block);

        MenuItem unblock = new MenuItem("Unblock");
        unblock.addActionListener(this);
        unblock.setActionCommand(UNBLOCK);
        actions.add(unblock);


        MenuItem frames = new MenuItem("Frames");
        frames.addActionListener(this);
        frames.setActionCommand(FRAMES);
        actions.add(frames);



        MenuItem send = new MenuItem("Send a message");
        send.addActionListener(this);
        send.setActionCommand(SEND);
        actions.add(send);

        menu.add(actions);
        this.setMenuBar(menu);
        this.setContentPane(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals(BLOCK)) {
            this.client.sendMessage(new BlockMessage());
        } else if (command.equals(UNBLOCK)) {
            this.client.sendMessage(new UnblockMessage());
        } else if (command.equals(FRAMES)) {
            String qnt = JOptionPane.showInputDialog(null, "Number of frames per second:");
            int q;
            try {
                q = Integer.parseInt(qnt);
            } catch (NumberFormatException c) {
                c.printStackTrace();
                return;
            }
            this.frames = q;
            this.client.sendMessage(new ChangeFrames(q));
        } else if (command.equals(SEND)) {
            String str = JOptionPane.showInputDialog(null, "Type the message:");
            this.client.sendMessage(new InfoMessage(str));
        }
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
        this.client.sendMessage(new ResizeScreenshot(new Rectangle(width, height)));
        this.client.sendMessage(new ChangeFrames(this.frames));
        while (!this.stopped) {
            t = (t++) % this.frames;
            if (t == 0 && (panel.getWidth() != width || panel.getHeight() != height)) {
                this.client.sendMessage(new ResizeScreenshot(new Rectangle(panel.getWidth(), panel.getHeight())));
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
                Thread.sleep(1000/frames);
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
