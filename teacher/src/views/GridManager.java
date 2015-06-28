package views;

import javax.imageio.ImageIO;
import javax.management.JMException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import communication.ClientThread;
import communication.ConnectorThread;
import messages.*;

/*!
 * Gerenciador em uma janela que permite que o servidor
 * veja as varias capturas de imagens dos computadores dos 
 * clientes 
 */
public class GridManager extends JFrame implements Runnable, ActionListener {
    ConnectorThread _st;
    GroupLayout layout;
    JPanel jp;
    JScrollPane js;
    boolean stopped;
    private int quantity;
    private int fpsBigScreen;
    private int fpsOverview;
    private static final String REGRID = "REGRID";
    private static final String FRAMEBG = "FRAMEBG";
    private static final String FRAMEST = "FRAMEST";
    private static final String SEND = "SEND";
    private static final String BLOCKALL = "BLOCKALL";
    private static final String UNBLOCKALL = "UNBLOCKALL";

    HashMap<String, JButton> buttons;

   public GridManager(ConnectorThread st) {
        super("LabSpy - Overview");
        quantity = 3;
        fpsBigScreen = 30;
        fpsOverview = 1;
        _st = st;
        jp = new JPanel();
        js = new JScrollPane(jp);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        layout = new GroupLayout(jp);
        jp.setLayout(layout);
        JMenuBar bar = new JMenuBar();
        this.setJMenuBar(bar);
        JMenu menu = new JMenu("Functions");
        bar.add(menu);

        JMenuItem itemRegrid = new JMenuItem("Regrid");
        itemRegrid.setActionCommand(REGRID);
        itemRegrid.addActionListener(this);
        menu.add(itemRegrid);

        JMenuItem itemFramesST = new JMenuItem("FPS Overview");
        itemFramesST.setActionCommand(FRAMEST);
        itemFramesST.addActionListener(this);
        menu.add(itemFramesST);

        JMenuItem itemFramesBG = new JMenuItem("FPS BigScreen");
        itemFramesBG.setActionCommand(FRAMEBG);
        itemFramesBG.addActionListener(this);
        menu.add(itemFramesBG);

        JMenuItem itemSend = new JMenuItem("Send Message");
        itemSend.setActionCommand(SEND);
        itemSend.addActionListener(this);
        menu.add(itemSend);



       JMenuItem itemBlock = new JMenuItem("Block All");
       itemBlock.setActionCommand(BLOCKALL);
       itemBlock.addActionListener(this);
       menu.add(itemBlock);

       JMenuItem itemUnblock = new JMenuItem("Unblock All");
       itemUnblock.setActionCommand(UNBLOCKALL);
       itemUnblock.addActionListener(this);
       menu.add(itemUnblock);

        stopped = false;
        this.buttons = new HashMap<>();
        this.setContentPane(js);
        this.setSize(400, 400);
        this.update();

    }


    public void stop() {
        this.stopped = true;
    }

    @Override
    public void run() {
        this.stopped = false;
        while (!this.stopped) {
            if (this.isActive()) {
                this.update();
            }
            try {
                Thread.sleep(1000/fpsOverview);
            } catch (InterruptedException e) {
                continue;
            }
        }
        this._st.stop();
    }

    @Override
    public void dispose() {
        super.dispose();
        this.stopped = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Collection<ClientThread> clients = _st.getClients();
        String command = e.getActionCommand();
        String ip = "";
        String str = "";
        int q = 0;
        if (command.equals(REGRID)) {
            String qnt = JOptionPane.showInputDialog(null, "Number of Columns:");
            try {
                q = Integer.parseInt(qnt);
            } catch (NumberFormatException c) {
                c.printStackTrace();
            }
            this.setQuantity(q);
            return;
        }
        else if (command.equals(FRAMEST)) {
            ip = JOptionPane.showInputDialog(null, "Type the IP:");
        }
        else if (command.equals(SEND)) {
            str = JOptionPane.showInputDialog(null, "Type the message:");
        }
        else if (command.equals(FRAMEBG)) {
            String qnt = JOptionPane.showInputDialog(null, "Number of frames per second(for the BigScreen):");
            try {
                q = Integer.parseInt(qnt);
            } catch (NumberFormatException c) {
                c.printStackTrace();
                return;
            }
            if (q <= 0) {
                q = 1;
            }
            this.fpsBigScreen = q;
            return;
        } else if (command.equals(FRAMEST)) {
            String qnt = JOptionPane.showInputDialog(null, "Number of frames per second (for the Overview):");
            try {
                q = Integer.parseInt(qnt);
            } catch (NumberFormatException c) {
                c.printStackTrace();
                return;
            }
            if (q <= 0) {
                q = 1;
            }
            this.fpsOverview = q;
        }
        for (ClientThread client : clients) {
            if (client.getComputer().getIp().equals(e.getActionCommand())) {
                BigScreen big = new BigScreen(client, fpsBigScreen);
                Thread thread = new Thread(big);
                thread.start();
                break;
            } else if (command.equals(FRAMEST)) {
                client.sendMessage(new ChangeFrames(this.fpsOverview));
            } else if (command.equals(SEND)) {
                client.sendMessage(new InfoMessage(str));
            } else if (command.equals(BLOCKALL)) {
                client.sendMessage(new BlockMessage());
            }  else if (command.equals(UNBLOCKALL)) {
                client.sendMessage(new UnblockMessage());
            }
        }
    }

    public void update() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null) {
            classLoader = Class.class.getClassLoader();
        }
        Collection<ClientThread> clients = _st.getClients();
        GroupLayout.ParallelGroup hg = layout.createParallelGroup();
        GroupLayout.SequentialGroup vg = layout.createSequentialGroup();
        GroupLayout.ParallelGroup _verticalGroup = layout.createParallelGroup();
        GroupLayout.SequentialGroup _horizontalGroup = layout.createSequentialGroup();
        int count = 0;
        ArrayList<String> seenIP = new ArrayList<>();
        for (ClientThread cl : clients) {
            if (count % quantity == 0) {
                    _horizontalGroup = layout.createSequentialGroup();
                    _verticalGroup = layout.createParallelGroup();
                    hg.addGroup(_horizontalGroup);
                    vg.addGroup(_verticalGroup);
            }

            if (cl.getLastScreenshot() == null || !cl.isRunning()) {
                continue;
            }
            String ip = cl.getComputer().getIp();
            seenIP.add(ip);
            JButton lb;
            if (this.buttons.containsKey(ip)) {
                lb = this.buttons.get(ip);
                lb.setIcon(new ImageIcon(cl.getLastScreenshot().getImage().getScaledInstance(
                        400,
                        300,
                        Image.SCALE_FAST
                )));
            } else {
                lb = new JButton(new ImageIcon(cl.getLastScreenshot().getImage().getScaledInstance(
                        400,
                        300,
                        Image.SCALE_FAST
                )));
                lb.setActionCommand(ip);
                lb.addActionListener(this);
                lb.setToolTipText(ip);
                this.buttons.put(ip, lb);
            }
            _horizontalGroup.addComponent(lb);
            _verticalGroup.addComponent(lb);
            count++;
        }
        BufferedImage img = null;
        try {
            if (img == null) {
                img = ImageIO.read(classLoader.getResourceAsStream("imagens/labspy400x400.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String ip: this.buttons.keySet()) {
            if (!seenIP.contains(ip)) {
                JButton b = this.buttons.get(ip);
                b.setIcon(new ImageIcon(img));
                break;
            }
        }
        layout.setHorizontalGroup(hg);
        layout.setVerticalGroup(vg);
        jp.revalidate();
        jp.repaint();
    }
    private void setQuantity(int qnt) {
        this.quantity = qnt;
        this.update();
    }
}
