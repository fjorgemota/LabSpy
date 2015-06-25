import communication.ClientThread;

import messages.StartScreenshot;
import remote_control.RobotThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


public class Main {
    public static void main(String[] argv) {
        try {
            RobotThread robot= new RobotThread();
            Thread robotThread = new Thread(robot);
            robotThread.setName("RobotThread");
            robotThread.start();
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress("0.0.0.0", 9500));
            Selector selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    SocketChannel s = server.accept();
                    s.configureBlocking(false);
                    ClientThread cl = new ClientThread(s, robot);
                    cl.run();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
