package views;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

import communication.ClientThread;
import communication.ConnectorThread;

/*!
 * Gerenciador em uma janela que permite que o servidor
 * veja as varias capturas de imagens dos computadores dos 
 * clientes 
 */
public class GridManager extends JFrame implements Runnable {
    ConnectorThread _st;
    GroupLayout layout;
    JPanel jp;
    JScrollPane js;
    boolean stopped;

   public GridManager(ConnectorThread st) {
        super("Overview");
        _st = st;
        jp = new JPanel();
        js = new JScrollPane(jp);
       this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        layout = new GroupLayout(jp);
        jp.setLayout(layout);
        stopped = false;
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
                Thread.sleep(1000/40);
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

    //    public void update() { //recriar o grid
//        GroupLayout layout = this.getLayout();
//        this.setHorizontalGroup(layout.createSequentialGroup());
//        this.setVerticalGroup(layout.createParallelGroup());
//        this.repaint();
//    }

    public void update() {
        Collection<ClientThread> clients = _st.getClients();
        GroupLayout.ParallelGroup hg = layout.createParallelGroup();
        GroupLayout.SequentialGroup vg = layout.createSequentialGroup();
        GroupLayout.ParallelGroup _verticalGroup = layout.createParallelGroup();
        GroupLayout.SequentialGroup _horizontalGroup = layout.createSequentialGroup();
        int count = 0;
        for (ClientThread cl : clients) {
            if (count % 4 == 0) {
                _horizontalGroup = layout.createSequentialGroup();
                _verticalGroup = layout.createParallelGroup();
                hg.addGroup(_horizontalGroup);
                vg.addGroup(_verticalGroup);

            }
            //BufferedImage image;
            //try {
            //    image = cl.getLastScreenshot().getImage();
            //} catch (IOException e) {
            //    JOptionPane.showMessageDialog(null, "Erro ao carregar imagem: e/home/podesta/Potaria/IyrK98Tb3_med.jpg");
            //    return;
            //}
            //image = resize(image, 400, 400);

            if (cl.getLastScreenshot() == null) {
                continue;
            }
            System.out.println("Processando cliente "+count);
            JLabel lb = new JLabel(cl.getLastScreenshot().getImage());
            _horizontalGroup.addComponent(lb);
            _verticalGroup.addComponent(lb);
            count++;
        }
        jp.removeAll();
        layout.setHorizontalGroup(hg);
        layout.setVerticalGroup(vg);
        jp.revalidate();
        jp.repaint();
    }
}
