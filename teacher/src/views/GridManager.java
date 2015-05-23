package views;

import javax.swing.*;
import java.util.ArrayList;
import communication.BaseClientThread;
import communication.ClientThread;
import communication.ServerThread;
import messages.StartScreenshot;

/*!
 * Gerenciador em uma janela que permite que o servidor
 * veja as varias capturas de imagens dos computadores dos 
 * clientes 
 */
public class GridManager extends JFrame {
    ServerThread _st;
    GroupLayout layout;

    JPanel jp;
    JScrollPane js;
    GroupLayout.ParallelGroup _verticalGroup;
    GroupLayout.SequentialGroup _horizontalGroup;

   public GridManager(ServerThread st) {
        super("Manager");
        _st = st;
        jp = new JPanel();

        layout = new GroupLayout(jp);
        js =  new JScrollPane(jp);
        jp.setLayout(layout);
        this.setContentPane(js);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        this.update();

    }

//    public void update() { //recriar o grid
//        GroupLayout layout = this.getLayout();
//        this.setHorizontalGroup(layout.createSequentialGroup());
//        this.setVerticalGroup(layout.createParallelGroup());
//        this.repaint();
//    }

    public void update() {
        ArrayList<ClientThread> clients = _st.getClients();
        GroupLayout.ParallelGroup hg = layout.createParallelGroup();
        GroupLayout.SequentialGroup vg = layout.createSequentialGroup();
        jp.removeAll();
        _horizontalGroup = layout.createSequentialGroup();
        _verticalGroup = layout.createParallelGroup();
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
            System.out.println("Processando cliente "+count+"..");
            JLabel lb = new JLabel(cl.getLastScreenshot().getImage());
            _horizontalGroup.addComponent(lb);
            _verticalGroup.addComponent(lb);
            count++;
        }
        layout.setHorizontalGroup(hg);
        layout.setVerticalGroup(vg);
        jp.revalidate();
        jp.repaint();
        js.revalidate();
        js.repaint();
        this.revalidate();
        this.repaint();
    }
}
