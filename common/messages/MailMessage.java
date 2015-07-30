package messages;

public abstract class MailMessage implements BaseMessage {
	protected String message;

	public String getMessage() {
		return this.message;
	}
}