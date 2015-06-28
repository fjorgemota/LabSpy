package commands;

public abstract class OSCommands {
	private static String os = System.getProperty("os.name").toLowerCase();
	
	public static OSCommands getInstance() {
		if (os.contains("nux")) {
			LinuxCommands linux = new LinuxCommands();
			return linux;
		} else if (os.contains("win")) {
			WindowsCommands windows = new WindowsCommands();
			return windows;
		} else {
			DummyCommands dummy = new DummyCommands();
			return dummy;
		}
	}

    protected void execute(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public abstract void shutdown();
	public abstract void restart();
	public abstract void openBrowser(String url);
	public void executeCommand(String command) {
		this.execute(command);
	}

}