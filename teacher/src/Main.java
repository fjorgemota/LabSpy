import communication.ServerThread;
import views.GridManager;
/**
 * Created by fernando on 11/04/15.
 */
public class Main {
    public static void main(String[] args) {
		ServerThread serverThread = new ServerThread();
		Thread server = new Thread(serverThread);
		server.start();
		GridManager g = new GridManager(serverThread);
		g.setVisible(true);
		while(true) {
			g.update();
			try {
				Thread.sleep(1000);
			} catch (Exception e) {}
		}
	}
}
