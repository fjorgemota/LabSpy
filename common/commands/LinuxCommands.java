package commands;

public class LinuxCommands extends OSCommands {

	protected String shutdown() {
		return "sudo poweroff";
	}

	protected String restart() {
		return "sudo reboot";
	}
}