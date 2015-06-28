package communication;

import others.Computer;
import config.Config;
import messages.StartScreenshot;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.List;

/*!
 * Implementaçao de um servidor, que mantem contato
 * com um ou mais clientes.
 */
public class ConnectorThread implements Runnable {
    Config configuration;
    HashMap<String, ClientThread> connections;
    boolean stopped;

    public ConnectorThread(Config configuration) {
        this.configuration = configuration;
        this.connections = new LinkedHashMap<String, ClientThread>();
        this.stopped = false;
    }

    public void stop() {
        this.stopped = true;
        Collection<ClientThread> clients = this.getClients();
        for(ClientThread client: clients) {
            client.stop();
        }
    }

    @Override
    public void run() {
        this.stopped = false;
        this.connections.clear();
        while (!this.stopped) {
            ArrayList<Computer> computers = new ArrayList<Computer>(this.configuration.getComputers());
            ArrayList<String> seenIP = new ArrayList<String>();
            for(Computer computer: computers) {
                seenIP.add(computer.getIp());
                if (this.connections.containsKey(computer.getIp()) && this.connections.get(computer.getIp()).isRunning()) {
                    continue;
                }
                SocketChannel connection;
                try {
                    connection = SocketChannel.open(new InetSocketAddress(computer.getIp(), 9500));
                    connection.configureBlocking(false);
                } catch (IOException e) {
                    connection = null;
                }
                ClientThread client = new ClientThread(connection, computer);
                client.sendMessage(new StartScreenshot(new Rectangle(400, 300)));
                if (client.isRunning()) {
                    Thread clientThread = new Thread(client);
                    clientThread.start();
                }
                this.connections.put(computer.getIp(), client);
            }
            /* Clean old connections */
            Set<Map.Entry<String, ClientThread>> it = this.connections.entrySet();
            ArrayList<String> toRemove = new ArrayList<String>();
            for(Map.Entry pair: it) {
                ClientThread client = (ClientThread) pair.getValue();
                if (!seenIP.contains((String) pair.getKey())) {
                    client.stop();
                    toRemove.add((String) pair.getKey());
                }
            }
            for(String key: toRemove) {
                this.connections.remove(key);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

    }
    public Collection<ClientThread> getClients() {
        return connections.values();
    }
}
