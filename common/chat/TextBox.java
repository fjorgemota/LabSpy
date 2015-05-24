package chat;

import javax.swing.*;
import javax.swing.JScrollPane;
import java.lang.String;

/*!
 * Gerador de uma janela de texto onde as mensagens
 * serao escritas e enviadas a um outro usuario
 * em que se esta em contato
 */
public class TextBox extends JPanel {
	private JButton send;
	private JTextArea textArea;
	private JScrollPane scroll;
	private final String SEND = "SEND";

	public TextBox() {
		this.send = new JButton("Send");
		this.textArea = new JTextArea(5, 20);
		this.scroll = new JScrollPane(
			textArea,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		this.scroll.setBounds(120, 50, 300, 100);
		this.textArea.setLineWrap(true);
		this.add(scroll);
		this.add(send);
	}
}