package communication;

import config.Computer;
import messages.BaseMessage;
import messages.InfoMessage;
import messages.Screenshot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/*!
 * Classe que implementa as remote_control referentes ao cliente.
 * Um cliente se comunicara com um servidor via envio de 
 * mensagens via sockets.
 */
public class ClientThread extends BaseClientThread {
    private Screenshot lastScreenshot;
    private InfoMessage info;
    private Computer computer;

    public ClientThread(SocketChannel sock, Computer computer) {
        super(sock);
        this.computer = computer;
        this.info = null;
        this.lastScreenshot = null;
    }

    protected void receiveMessage(BaseMessage msg) {
        if (msg instanceof Screenshot) {
            this.lastScreenshot = (Screenshot) msg;
        } else if (msg instanceof InfoMessage) {
            this.info = (InfoMessage) msg;
        }
    }

    public Screenshot getLastScreenshot() {
        return this.lastScreenshot;
    }

    public InfoMessage getInfo() {
        return this.info;
    }

    public Computer getComputer() {
        return computer;
    }
}
