package communication;

import config.Computer;
import config.Config;
import messages.StartScreenshot;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/*!
 * Implementaçao de um servidor, que mantem contato
 * com um ou mais clientes.
 */
public class ConnectorThread implements Runnable {
    Config configuration;
    HashMap<String, ClientThread> connections;

    public ConnectorThread(Config configuration) {
        this.configuration = configuration;
        this.connections = new HashMap<>();
    }
    @Override
    public void run() {
        while (true) {
            List<Computer> computers = this.configuration.getComputers();
            ArrayList<String> seenIP = new ArrayList<>();
            for(Computer computer: computers) {
                seenIP.add(computer.getIp());
                System.out.println("Checking if "+computer.getIp()+" is in the connections dictionary");
                if (this.connections.containsKey(computer.getIp())) {
                    System.out.println("Avoiding connection");
                    continue;
                }
                SocketChannel connection;
                try {
                    connection = SocketChannel.open(new InetSocketAddress(computer.getIp(), 9500));
                    connection.configureBlocking(false);
                } catch (IOException e) {
                    continue;
                }
                ClientThread client = new ClientThread(connection);
                client.sendMessage(new StartScreenshot());
                Thread clientThread = new Thread(client);
                clientThread.start();
                System.out.println("Putting "+computer.getIp()+" in hashmap");
                this.connections.put(computer.getIp(), client);
            }
            /* Clean old connections */
            Set<Map.Entry<String, ClientThread>> it = this.connections.entrySet();
            ArrayList<String> toRemove = new ArrayList<>();
            for(Map.Entry pair: it) {
                ClientThread client = (ClientThread) pair.getValue();
                System.out.println("Checking if "+pair.getKey()+" is running or is not deleted: "+(!client.isRunning() +"||"+ !seenIP.contains((String) pair.getKey())));
                if (!client.isRunning() || !seenIP.contains((String) pair.getKey())) {
                    client.stop();
                    System.out.println("Putting "+pair.getKey()+" in remove list");
                    toRemove.add((String) pair.getKey());
                }
            }
            for(String key: toRemove) {
                System.out.println("Removing key "+key);
                this.connections.remove(key);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                continue;
            }
        }

    }
    public Collection<ClientThread> getClients() {
        return connections.values();
    }
}
