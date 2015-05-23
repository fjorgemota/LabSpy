package communication;

import messages.*;

import javax.crypto.CipherInputStream;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*!
 * Classe que implementa o sistema de comunicação básico entre cliente/servidor.
 */
public abstract class BaseClientThread implements Runnable {
    private SocketChannel sock;
    private BlockingQueue<ByteBuffer> send;

    public BaseClientThread(SocketChannel sock) {
        this.sock = sock;
        this.send = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        System.out.println("Conectando");
        try {
            Selector selector = Selector.open();
            this.sock.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            ByteBuffer readBuf = null;
            ByteBuffer writeBuf = null;

            int readSize = -1;
            while (true) {
                int num = selector.select();
                if (num == 0) {
                    continue;
                }
                if (!this.sock.isConnected()) {
                    break;
                }
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while(keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isReadable()) {
                        if (readBuf == null) {
                            System.out.println("Allocating 128 bytes for receiving the size of the request");
                            readBuf = ByteBuffer.allocate(128);
                        }
                        this.sock.read(readBuf);
                        if (readBuf.remaining() == 0) { // If there's no remaining buffer
                            readBuf.rewind();
                            if (readSize == -1) {
                                ByteArrayInputStream bias = new ByteArrayInputStream(readBuf.array());
                                ObjectInputStream interpreter = new ObjectInputStream(bias);
                                readSize = interpreter.readInt();
                                System.out.println("Allocating "+readSize+" bytes to read the next object");
                                readBuf = ByteBuffer.allocate(readSize);
                            } else {
                                ByteArrayInputStream bias = new ByteArrayInputStream(readBuf.array());
                                ObjectInputStream interpreter = new ObjectInputStream(bias);
                                this.receiveMessage((BaseMessage) interpreter.readObject());
                                System.out.println("Received message..");
                                readBuf = null;
                                readSize = -1;
                            }
                        }
                    }
                    if (key.isWritable()) {
                        if (writeBuf == null) {
                            writeBuf = this.send.poll();
                        }
                        if (writeBuf == null) {
                            continue;
                        }
                        this.sock.write(writeBuf);
                        if (writeBuf.remaining() == 0) {
                            writeBuf = null;
                        }

                        System.out.println("Message sended..");
                    }
                }
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
        try {
            System.out.println("Writing object..");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(baos);
            os.writeObject(message);
            System.out.println("Flushing..");
            os.flush();

            ByteArrayOutputStream baosSize = new ByteArrayOutputStream();
            ObjectOutputStream osSize = new ObjectOutputStream(baosSize);
            osSize.writeInt(baos.size());
            System.out.println("Flushing..");
            osSize.flush();
            ByteBuffer buf = ByteBuffer.allocate(128);
            buf.put(baosSize.toByteArray());
            buf.rewind();
            this.send.add(buf);


            buf = ByteBuffer.allocate(baos.size());
            buf.put(baos.toByteArray());
            buf.rewind();
            this.send.add(buf);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
