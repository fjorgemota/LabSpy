package views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import threads.ClientThread;
import threads.ServerThread;

/**
 * Created by podesta on 11/04/15.
 */
public class GridManager extends JFrame {
    GridManager(ServerThread st) {
        super("Manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        JPanel jp = new JPanel();
        JScrollPane js = new JScrollPane(jp);
        GroupLayout layout = new GroupLayout(jp);
        jp.setLayout(layout);
        this.setContentPane(js);
        ArrayList<ClientThread> clients = st.getClients();
        GroupLayout.ParallelGroup hg = layout.createParallelGroup();
        GroupLayout.SequentialGroup vg = layout.createSequentialGroup();

        GroupLayout.SequentialGroup horizontalGroup = layout.createSequentialGroup();
        GroupLayout.ParallelGroup verticalGroup = layout.createParallelGroup();
        int count = 0;
        for (ClientThread cl : clients) {
            if (count % 4 == 0) {
                horizontalGroup = layout.createSequentialGroup();
                verticalGroup = layout.createParallelGroup();
                hg.addGroup(horizontalGroup);
                vg.addGroup(verticalGroup);

            }
            //BufferedImage image;
            //try {
            //    image = cl.getLastScreenshot().getImage();
            //} catch (IOException e) {
            //    JOptionPane.showMessageDialog(null, "Erro ao carregar imagem: e/home/podesta/Potaria/IyrK98Tb3_med.jpg");
            //    return;
            //}
            //image = resize(image, 400, 400);
            JLabel lb = new JLabel(cl.getLastScreenshot().getImage());
            horizontalGroup.addComponent(lb);
            verticalGroup.addComponent(lb);
            count++;
        }
        layout.setHorizontalGroup(hg);
        layout.setVerticalGroup(vg);
    }
    public BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
