import communication.ConnectorThread;
import config.Computer;
import config.Config;
import views.GridManager;
/**
 * Created by fernando on 11/04/15.
 */
public class Main {
    public static void main(String[] args) {
		Config configuration = Config.getInstance();
		if (configuration.getComputers().size() == 0) {
			configuration.addComputer(new Computer("10.0.0.12"));
			configuration.addComputer(new Computer("127.0.0.1"));
		}
		ConnectorThread connectorThread = new ConnectorThread(configuration);
		Thread connector = new Thread(connectorThread);
		connector.start();
		GridManager g = new GridManager(connectorThread);
		g.setVisible(true);
		while(true) {
			g.update();
			try {
				Thread.sleep(1000/40);
			} catch (Exception e) {}
		}
	}
}
