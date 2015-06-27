package commands;

public class LinuxCommands extends OSCommands {

	public String shutdown() {
		return "sudo poweroff";
	}

	public String restart() {
		return "sudo reboot";
	}
}