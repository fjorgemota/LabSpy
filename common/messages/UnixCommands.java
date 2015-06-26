package messages;

public class UnixCommands extends OSCommands {

	protected String shutdown() {
		return "sudo poweroff";
	}

	protected String restart() {
		return "sudo reboot";
	}
}