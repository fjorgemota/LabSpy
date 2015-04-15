package communication;

import messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*!
 * Classe que implementa o sistema de comunicação básico entre cliente/servidor.
 */
public abstract class BaseClientThread implements Runnable {
    private Socket sock;
    private SenderThread sender;

    public BaseClientThread(Socket sock) {
        this.sock = sock;
        this.sender = new SenderThread(sock);
    }

    @Override
    public void run() {
        System.out.println("Starting client thread..");
        Thread senderThread = new Thread(this.sender);
        senderThread.start();
        try {
            System.out.println("Creating input stream..");
            ObjectInputStream is = new ObjectInputStream(this.sock.getInputStream());
            while (true) {
                System.out.println("Receiving message..");
                BaseMessage msg = (BaseMessage) is.readObject();
                System.out.println("Received message..");
                this.receiveMessage(msg);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e2) {
            e2.printStackTrace();
        }
        System.out.println("Finishing client thread..");
    }

    protected abstract void receiveMessage(BaseMessage msg);

    public void sendMessage(BaseMessage message) {
        this.sender.sendMessage(message);
    }

}
