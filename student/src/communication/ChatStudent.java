package communication;

import chat.Messenger;
import messages.BaseMessage;

public class ChatStudent {
	private Messenger chat;

	public ChatStudent() {
		this.chat = new Messenger();
		chat.setVisible(true);
	}

	public void receiveMessage(String msg) {
        this.chat.addMessageOnTheScreen("Teacher: " + msg);
	}

	public void sendMessage(BaseClientThread client) {
		this.chat.sendMessage(client);
	}
}