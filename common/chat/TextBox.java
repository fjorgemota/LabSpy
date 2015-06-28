package chat;

import javax.swing.*;
import java.lang.String;

/*!
 * Gerador de uma janela de texto onde as mensagens
 * serao escritas e enviadas a um outro usuario
 * em que se esta em contato
 */
public class TextBox extends JPanel {
	private final String SEND = "SEND";

	public TextBox() {
		JButton send = new JButton("Send");
		JTextArea textArea = new JTextArea(5, 20);
		JScrollPane scroll = new JScrollPane(
				textArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		scroll.setBounds(120, 50, 300, 100);
		textArea.setLineWrap(true);
		this.add(scroll);
		this.add(send);
	}
}