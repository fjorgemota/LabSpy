package threads;

import messages.BaseMessage;
import messages.InfoMessage;
import messages.Screenshot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by fernando on 11/04/15.
 */
public class ClientThread implements Runnable {
    private Socket sock;
    private Queue<BaseMessage> send;
    private Screenshot lastScreenshot;
    private InfoMessage info;

    public ClientThread(Socket sock) {
        this.sock = sock;
        this.send = new ConcurrentLinkedQueue<BaseMessage>();
        this.info = null;
        this.lastScreenshot = null;
    }
    @Override
    public void run() {
        try {
            ObjectInputStream is = new ObjectInputStream(this.sock.getInputStream());
            ObjectOutputStream os = new ObjectOutputStream(this.sock.getOutputStream());
            while (true) {
               if(is.available() > 0) {
                    BaseMessage msg = (BaseMessage) is.readObject();
                    this.receiveMessage(msg);
                }
                else if (!this.send.isEmpty()) {
                    BaseMessage toSend = this.send.poll();
                    os.writeObject(toSend);
                    os.flush();
                }
            }
        } catch(IOException e) {
        } catch(ClassNotFoundException e) {}
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

    public void sendMessage(BaseMessage msg) {
        this.send.add(msg);
    }
}
