package threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*!
 * Implementaçao de um servidor, que mantem contato
 * com um ou mais clientes. O servidor pede requisiçoes
 * ao cliente e este concede as informaçoes
 */
public class ServerThread implements Runnable {
    private ArrayList<ClientThread> clients;

    public ServerThread() {
        this.clients = new ArrayList<ClientThread>();
    }
    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(9500);
            while (true) {
                Socket s = server.accept();
                ClientThread cl = new ClientThread(s);
                Thread client = new Thread(cl);
                client.start();
                this.clients.add(cl);
            }
        } catch (IOException e) {}
    }
    public ArrayList<ClientThread> getClients() {
        return clients;
    }
}
