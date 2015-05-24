package chat;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;

/*!
 * Classe que gera uma janela de bate-papo entre
 * o cliente e o servidor.
 */
public class Messenger extends JFrame {
    private TextBox textBox;
    private JScrollPane scroll;

    public Messenger() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        textBox = new TextBox();
        this.setTitle("Chat");
        JScrollPane scroll = new JScrollPane(panel);
        GroupLayout layout = new GroupLayout(panel);
        GroupLayout.SequentialGroup vertical = layout.createSequentialGroup();
        GroupLayout.ParallelGroup horizontal = layout.createParallelGroup();

        panel.setLayout(layout);
        this.setContentPane(scroll);

        for (int a = 0; a < 18; a++) {
            JLabel name = new JLabel();
            textBox.setPreferredSize(new Dimension(this.getWidth(), 75));
            vertical.addComponent(name);
            horizontal.addComponent(name);
        }

        vertical.addComponent(textBox);
        horizontal.addComponent(textBox);
        layout.setVerticalGroup(vertical);
        layout.setHorizontalGroup(horizontal);
    }
}