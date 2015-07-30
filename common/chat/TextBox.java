package chat;

import javax.swing.*;
import java.lang.String;
import java.awt.event.*;
import messages.ChatMessage;
import communication.BaseClientThread;

/*!
 * Gerador de uma janela de texto onde as mensagens
 * serao escritas e enviadas a um outro usuario
 * em que se esta em contato
 */
public class TextBox extends JPanel implements ActionListener, KeyListener {
	private final String SEND = "SEND";
	private JTextField textArea;
	private BaseClientThread client;
	private Messenger messenger;

	public TextBox() {
		JButton send = new JButton("Send");
		send.setActionCommand(SEND);
		send.addActionListener(this);

		this.textArea = new JTextField(35);
		JScrollPane scroll = new JScrollPane(
				this.textArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		scroll.setBounds(120, 50, 300, 100);
		this.textArea.addKeyListener(this);
		this.add(scroll);
		this.add(send);
	}

	public void setMessage(BaseClientThread client, Messenger mess) {
		this.client = client;
		this.messenger = mess;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals(SEND)) {
			String mess = this.textArea.getText();
			final ChatMessage answer = new ChatMessage(mess);
			this.messenger.addMessageOnTheScreen("Me: " +mess);
			this.client.sendMessage(answer);
			this.textArea.setText(null);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			String mess = this.textArea.getText();
			final ChatMessage answer = new ChatMessage(mess);
			this.messenger.addMessageOnTheScreen("Me: " +mess);
			this.client.sendMessage(answer);
			this.textArea.setText(null);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}