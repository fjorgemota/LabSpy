import threads.ServerThread;
import views.GridManager;
/**
 * Created by fernando on 11/04/15.
 */

public class Main {
    public static void main(String[] args) {
		GridManager g = new GridManager(new ServerThread());
		g.setVisible(true);
	}
}
