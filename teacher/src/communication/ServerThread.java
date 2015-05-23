package communication;

import messages.StartScreenshot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/*!
 * Implementaçao de um servidor, que mantem contato
 * com um ou mais clientes.
 */
public class ServerThread implements Runnable {
    private ArrayList<ClientThread> clients;

    public ServerThread() {
        this.clients = new ArrayList<ClientThread>();
    }
    @Override
    public void run() {
        try {
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
                ClientThread cl = new ClientThread(s);
                Thread client = new Thread(cl);
                client.start();
                cl.sendMessage(new StartScreenshot());
                this.clients.add(cl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<ClientThread> getClients() {
        return clients;
    }
}
