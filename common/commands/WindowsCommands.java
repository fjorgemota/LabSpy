package commands;

public class WindowsCommands extends OSCommands {

	public void shutdown() {
		this.execute("shutdown /s");
	}

	public void restart() {
		this.execute("shutdown /r");
	}

	public void openBrowser(String url) {
		this.execute("explorer \""+url+"\"");
	}
}