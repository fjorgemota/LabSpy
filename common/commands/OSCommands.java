package commands;

public abstract class OSCommands {
	private static String os = System.getProperty("os.name").toLowerCase();
	
	public static OSCommands getInstance() {
		if (os.indexOf("nux") >= 0) {
			LinuxCommands linux = new LinuxCommands();
			return linux;
		} else if (os.indexOf("win") >= 0) {
			WindowsCommands windows = new WindowsCommands();
			return windows;
		} else {
			return null;
		}
	}

	public abstract String shutdown();
	public abstract String restart();
}