package chat;

import javax.swing.*;
import java.awt.*;

/*!
 * Classe que gera uma janela de bate-papo entre
 * o cliente e o servidor.
 */
public class Messenger extends JFrame {

    public Messenger() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 400);

        JPanel panel = new JPanel();
        JTextArea textArea = new JTextArea(10, 10);
        TextBox textBox = new TextBox();
        this.setTitle("Chat");
        textArea.setEditable(true);
        JScrollPane scroll = new JScrollPane(panel);
        GroupLayout layout = new GroupLayout(panel);
        GroupLayout.SequentialGroup vertical = layout.createSequentialGroup();
        GroupLayout.ParallelGroup horizontal = layout.createParallelGroup();

        panel.setLayout(layout);
        this.setContentPane(scroll);

        // Output messages
        JTextField userOutputField = new JTextField();
        userOutputField.setEditable(false);
        vertical.addComponent(userOutputField);
        horizontal.addComponent(userOutputField);
        textBox.setPreferredSize(new Dimension(this.getWidth(), 75));

        vertical.addComponent(textBox);
        horizontal.addComponent(textBox);
        layout.setVerticalGroup(vertical);
        layout.setHorizontalGroup(horizontal);
        this.add(userOutputField, SwingConstants.CENTER);
    }
}