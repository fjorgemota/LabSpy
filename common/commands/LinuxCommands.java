package commands;

public class LinuxCommands extends OSCommands {

	public void shutdown() {
		this.execute("sudo poweroff");
	}

	public void restart() {
		this.execute("sudo reboot");
	}
}