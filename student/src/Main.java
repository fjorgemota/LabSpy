import communication.ClientThread;
import messages.StartScreenshot;
import remote_control.RobotThread;
import remote_control.ScreenshotThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by fernando on 11/04/15.
 */
public class Main {
    public static void main(String[] argv) {
        RobotThread robot = new RobotThread();
        Thread robotThread = new Thread(robot);
        robotThread.start();
        try {
            /** Turns student into a server */
            Selector selector = Selector.open();
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress("0.0.0.0", 9500));
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Awaiting connection..");
            while (true) {
                int num = selector.select();
                if (num == 0) {
                    continue;
                }
                SocketChannel s = server.accept();
                s.configureBlocking(false);
                System.out.println("Connection accepted");

                ClientThread client = new ClientThread(s, robot);

                Thread clientThread = new Thread(client);
                clientThread.start();

                try {
                    clientThread.join();
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
