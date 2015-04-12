import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by podesta on 11/04/15.
 */
public class GridManager extends JFrame{
        super("Manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        JPanel jp = new JPanel();
        JScrollPane js = new JScrollPane(jp);
        GroupLayout layout = new GroupLayout(jp);
        jp.setLayout(layout);
        this.setContentPane(js);
        GroupLayout.ParallelGroup hg = layout.createParallelGroup();
        GroupLayout.SequentialGroup vg = layout.createSequentialGroup();
        for(int i = 0; i < 5; i++) {
            GroupLayout.SequentialGroup horizontalGroup = layout.createSequentialGroup();
            GroupLayout.ParallelGroup verticalGroup = layout.createParallelGroup();

            for (int j = 0; j < 4; j++) {
                BufferedImage image;
                try {
                    image = ImageIO.read(new File("/home/podesta/Potaria/eIyrK98Tb3_med.jpg"));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Erro ao carregar imagem: e/home/podesta/Potaria/IyrK98Tb3_med.jpg");
                    return;
                }
                image = resize(image, 400, 400);
                JLabel lb = new JLabel(new ImageIcon(image));
                horizontalGroup.addComponent(lb);
                verticalGroup.addComponent(lb);
            }
            hg.addGroup(horizontalGroup);
            vg.addGroup(verticalGroup);
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
