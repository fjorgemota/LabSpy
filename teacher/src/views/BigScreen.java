package views;

import communication.ClientThread;
import messages.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

/**
 * Created by fernando on 24/05/15.
 */
public class BigScreen extends JFrame implements Runnable, ActionListener, KeyListener {
    private ClientThread client;
    private boolean stopped;
    private int frames;
    static final String SEND = "SEND";
    static final String FRAMES = "FRAMES";
    static final String BLOCK = "BLOCK";
    static final String UNBLOCK = "UNBLOCK";
    static final String OPEN_BROWSER = "OPEN_BROWSER";
    static final String COMMAND = "COMMAND";
    static final String SHUTDOWN = "SHUTDOWN";
    static final String RESTART = "RESTART";

    private BigScreenPanel panel;

    public BigScreen(ClientThread client, int fps) {
        super("LabSpy - BigScreen - "+client.getComputer().getIp()+" - "+client.getComputer().getLabel());
        this.frames = fps;
        this.client = client;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setSize(800, 600);
        panel = new BigScreenPanel(client);

        JMenuBar menu = new JMenuBar();
        JMenu actions = new JMenu("Actions");
        JMenuItem block = new JMenuItem("Block");
        block.addActionListener(this);
        block.setActionCommand(BLOCK);
        actions.add(block);

        JMenuItem unblock = new JMenuItem("Unblock");
        unblock.addActionListener(this);
        unblock.setActionCommand(UNBLOCK);
        actions.add(unblock);


        JMenuItem frames = new JMenuItem("Frames");
        frames.addActionListener(this);
        frames.setActionCommand(FRAMES);
        actions.add(frames);

        JMenuItem send = new JMenuItem("Send a message");
        send.addActionListener(this);
        send.setActionCommand(SEND);
        actions.add(send);


        JMenuItem openBrowser = new JMenuItem("Open Browser in URL");
        openBrowser.addActionListener(this);
        openBrowser.setActionCommand(OPEN_BROWSER);
        actions.add(openBrowser);


        JMenuItem cmd = new JMenuItem("Send Command");
        cmd.addActionListener(this);
        cmd.setActionCommand(COMMAND);
        actions.add(cmd);



        JMenuItem shutdown = new JMenuItem("Shutdown");
        shutdown.addActionListener(this);
        shutdown.setActionCommand(SHUTDOWN);
        actions.add(shutdown);

        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(this);
        restart.setActionCommand(RESTART);
        actions.add(restart);

        menu.add(actions);
        this.setJMenuBar(menu);
        this.setContentPane(panel);
        this.addKeyListener(this);
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
            if (str.trim().isEmpty()) {
                return;
            }
            this.client.sendMessage(new InfoMessage(str));
        } else if (command.equals(OPEN_BROWSER)) {
            String url = JOptionPane.showInputDialog(null, "Type the URL to Open:");
            if (url.trim().isEmpty()) {
                return;
            }
            this.client.sendMessage(new OpenBrowserMessage(url));
        } else if (command.equals(COMMAND)) {
            String cmd = JOptionPane.showInputDialog(null, "Type the Command to Execute:");
            if (cmd.trim().isEmpty()) {
                return;
            }
            this.client.sendMessage(new CustomCommandMessage(cmd));
        } else if (JOptionPane.showConfirmDialog(null, "You're sure that you want to shutdown/restart this computer?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (command.equals(SHUTDOWN)) {
                client.sendMessage(new ShutdownMessage());
            } else if (command.equals(RESTART)) {
                client.sendMessage(new RestartMessage());
            }
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
        MouseMoveMessage position = new MouseMoveMessage(x, y, width, height);

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
                    position = p;
                }
                if (t == 0 && (position.getX() != x || position.getY() != y)) {
                    this.client.sendMessage(position);
                    x = position.getX();
                    y = position.getY();
                }
                this.repaint();
            }
            try {
                Thread.sleep(1000/frames);
            } catch (InterruptedException ignored) {
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
}
