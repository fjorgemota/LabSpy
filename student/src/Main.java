import communication.ClientThread;
import config.Config;
import remote_control.RobotThread;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by fernando on 11/04/15.
 */
public class Main {
    public static void main(String[] argv) {
        Config config = Config.getInstance();
        RobotThread robot = new RobotThread();
        Thread robotThread = new Thread(robot);
        robotThread.start();
        Socket sock;
        try {
            sock = new Socket(config.getAddress(), 9500);
        } catch(IOException e1) {
            e1.printStackTrace();
            return;
        }
        ClientThread client = new ClientThread(sock, robot);
        Thread clientThread = new Thread(client);
        clientThread.start();
        try {
            clientThread.join();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }
}
