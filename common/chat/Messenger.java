package chat;

import javax.swing.*;
import java.awt.*;
import communication.BaseClientThread;

/*!
 * Classe que gera uma janela de bate-papo entre
 * o cliente e o servidor.
 */
public class Messenger extends JFrame {

    private GroupLayout.SequentialGroup vertical;
    private GroupLayout.ParallelGroup horizontal;
    private TextBox textBox;

    public Messenger() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(500, 400);
        this.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        JTextArea textArea = new JTextArea(10, 10);
        this.textBox = new TextBox();
        this.setTitle("Chat");
        textArea.setEditable(true);
        JScrollPane scroll = new JScrollPane(panel);
        GroupLayout layout = new GroupLayout(panel);
        this.vertical = layout.createSequentialGroup();
        this.horizontal = layout.createParallelGroup();

        panel.setLayout(layout);
        scroll.setSize(new Dimension(this.getWidth(), this.getHeight() - 80));
        this.add(scroll, BorderLayout.CENTER);

        // Output messages
        JTextArea userOutputField = new JTextArea();
        userOutputField.setEditable(false);
        userOutputField.setLineWrap(true);
        userOutputField.setBackground(panel.getBackground());
        userOutputField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.vertical.addComponent(userOutputField);
        this.horizontal.addComponent(userOutputField);

        this.textBox.setSize(new Dimension(this.getWidth(), 75));

        this.add(this.textBox, BorderLayout.SOUTH);
        layout.setVerticalGroup(vertical);
        layout.setHorizontalGroup(horizontal);
    }

    public void addMessageOnTheScreen(String msg) {
        JPanel panel = new JPanel();
        JTextArea userOutputField = new JTextArea();
        userOutputField.setEditable(false);
        userOutputField.setLineWrap(true);
        userOutputField.setBackground(panel.getBackground());
        userOutputField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        userOutputField.setText(msg);
        this.vertical.addComponent(userOutputField);
        this.horizontal.addComponent(userOutputField);
        this.revalidate();
        this.repaint();
    }

    public void sendMessage(BaseClientThread client) {
        this.textBox.setMessage(client, Messenger.this);
    }
}