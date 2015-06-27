package commands;

public class WindowsCommands extends OSCommands {

	public String shutdown() {
		return "shutdown /s";
	}

	public String restart() {
		return "shutdown /r";
	}
}