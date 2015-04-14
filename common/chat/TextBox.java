package chat;

import javax.swing.*;
import javax.swing.JScrollPane;
import java.lang.String;

/**
 * created by caique on 11/04/15.
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
		this.add(scroll);
		this.add(send);
	}
}