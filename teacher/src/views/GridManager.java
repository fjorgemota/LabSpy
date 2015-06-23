package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import communication.ClientThread;
import communication.ConnectorThread;
import messages.RestartMessage;

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
    HashMap<String, JButton> buttons;

   public GridManager(ConnectorThread st) {
        super("LabSpy - Overview");
        _st = st;
        jp = new JPanel();
        js = new JScrollPane(jp);
       this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        layout = new GroupLayout(jp);
        jp.setLayout(layout);
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
                Thread.sleep(1000/30);
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
        for (ClientThread client: clients) {
            if (client.getComputer().getIp().equals(e.getActionCommand())) {
                BigScreen big = new BigScreen(client);
                Thread thread = new Thread(big);
                thread.start();
                break;
            }
        }

    }

    public void update() {
        Collection<ClientThread> clients = _st.getClients();
        GroupLayout.ParallelGroup hg = layout.createParallelGroup();
        GroupLayout.SequentialGroup vg = layout.createSequentialGroup();
        GroupLayout.ParallelGroup _verticalGroup = layout.createParallelGroup();
        GroupLayout.SequentialGroup _horizontalGroup = layout.createSequentialGroup();
        int count = 0;
        ArrayList<String> seenIP = new ArrayList<>();
        for (ClientThread cl : clients) {
            if (count % 4 == 0) {
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
                lb.setIcon(new ImageIcon(cl.getLastScreenshot().getImage().getImage().getScaledInstance(
                        400,
                        300,
                        Image.SCALE_FAST
                )));
            } else {
                lb = new JButton(new ImageIcon(cl.getLastScreenshot().getImage().getImage().getScaledInstance(
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
        boolean removeAll = false;
        for (String ip: this.buttons.keySet()) {
            if (!seenIP.contains(ip)) {
                removeAll = true;
                this.buttons.remove(ip);
            }
        }
        if (removeAll) {
            jp.removeAll();
        }
        layout.setHorizontalGroup(hg);
        layout.setVerticalGroup(vg);
        jp.revalidate();
        jp.repaint();
    }
}
