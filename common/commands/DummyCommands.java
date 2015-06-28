package commands;

public class DummyCommands extends OSCommands {
	DummyCommands() {
		System.out.println("OS unrecognized");
	}

	public void shutdown() {}
	public void restart() {}
	public void openBrowser(String url) {}
}