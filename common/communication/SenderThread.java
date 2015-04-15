package communication;

import messages.BaseMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fernando on 15/04/15.
 */
public class SenderThread implements Runnable {
    private Socket sock;
    private BlockingQueue<BaseMessage> send;

    public SenderThread(Socket sock) {
        this.sock = sock;
        this.send = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        try {
            System.out.println("Creating output stream..");
            ObjectOutputStream os = new ObjectOutputStream(this.sock.getOutputStream());
            while (true) {
                System.out.println("Sending message..");
                BaseMessage toSend = this.send.poll();
                System.out.println("Writing object..");
                os.writeObject(toSend);
                System.out.println("Flushing..");
                os.flush();
                System.out.println("Message sended..");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendMessage(BaseMessage msg) {
        System.out.println("Adding message to the queue..");
        this.send.add(msg);
        System.out.println("Added message to the queue..");
    }
}
