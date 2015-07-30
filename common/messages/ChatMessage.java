package messages;

public class ChatMessage implements BaseMessage {
    private String message;

    public ChatMessage(String str) {
        this.message = str;
    }

    public String getMessage() {
		return this.message;
	}
}