package commands;

public class OSCommands {
	private static String os = System.getProperty("os.name").toLowerCase();
	
	public static OSCommands getInstance() {
		if (this.os.indexOf("nix")) {
			LinuxCommands linux = new LinuxCommands();
			return linux;
		} else if (this.os.indexOf("win")) {
			WindowsCommands windows = new WindowsCommands();
			return windows;
		}
	}

	abstract String shutdown();
	abstract String restart();
}