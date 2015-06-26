package messages;

public class WindowsCommands extends OSCommands {

	protected String shutdown() {
		return "shutdown /s";
	}

	protected String restart() {
		return "shutdown /r";
	}
}