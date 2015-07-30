package communication;

import chat.Messenger;
import messages.BaseMessage;

public class ChatTeacher {
	private Messenger chat;

	public ChatTeacher() {
		this.chat = new Messenger();
		chat.setVisible(true);
	}

	public void receiveMessage(String msg) {
        this.chat.addMessageOnTheScreen("Student: " + msg);
	}

	public void sendMessage(BaseClientThread client) {
		this.chat.sendMessage(client);
	}
}