package communication;

import messages.StartScreenshot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
            ServerSocket server = new ServerSocket(9500);
            while (true) {
                System.out.println("Awaiting connection..");
                Socket s = server.accept();
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
