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

    protected void execute(String command) {
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public abstract void shutdown();
	public abstract void restart();
}