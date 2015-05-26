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
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/*!
 * Classe que implementa o sistema de comunicação básico entre cliente/servidor.
 */
public abstract class BaseClientThread implements Runnable {
    private SocketChannel sock;
    private boolean stopped;
    private BlockingQueue<ByteBuffer> send;
    private SelectionKey key;
    private Selector selector;

    public BaseClientThread(SocketChannel sock) {
        this.sock = sock;
        this.stopped = false;
        this.key = null;
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.send = new LinkedBlockingQueue<>(100);
    }

    public void stop() {
        this.stopped = true;
        System.out.println("Parando BaseClientThread");
    }

    public boolean isRunning() {
        return !this.stopped && !this.sock.socket().isClosed();
    }
    @Override
    public void run() {
        try {
            this.key = this.sock.register(this.selector, SelectionKey.OP_READ);

            ByteBuffer readBuf = null;
            ByteBuffer writeBuf = null;
            boolean toWrite = false;

            int readSize = -1;
            while (!this.stopped) {
                this.selector.select();
                Set<SelectionKey> keys = this.selector.selectedKeys();
                for (SelectionKey key: keys) {
                    if (key.isReadable()) {
                        if (readBuf == null) {
                            readBuf = ByteBuffer.allocate(128);
                        }
                        if (this.sock.read(readBuf) == -1) {
                            this.stop();
                        }
                        if (!readBuf.hasRemaining()) { // If there's no remaining buffer
                            readBuf.rewind();
                            if (readSize == -1) {
                                ByteArrayInputStream bias = new ByteArrayInputStream(readBuf.array());
                                ObjectInputStream interpreter = new ObjectInputStream(bias);
                                readSize = interpreter.readInt();
                                readBuf = ByteBuffer.allocate(readSize);
                            } else {
                                ByteArrayInputStream bias = new ByteArrayInputStream(readBuf.array());
                                GZIPInputStream gzip = new GZIPInputStream(bias, 10240);
                                ObjectInputStream interpreter = new ObjectInputStream(gzip);
                                BaseMessage message = (BaseMessage) interpreter.readUnshared();
                                System.out.println("Lendo mensagem " + message);
                                this.receiveMessage(message);
                                readBuf = null;
                                readSize = -1;
                            }
                        }
                    }
                    if (key.isWritable()) {
                        if (writeBuf == null) {
                            System.out.println("Removendo mensagem da lista de mensagens enviadas");
                            writeBuf = this.send.poll();
                        }
                        if (writeBuf == null) {
                            continue;
                        }
                        System.out.println("Writing..");
                        this.sock.write(writeBuf);
                        System.out.println("Writed!");
                        if (!writeBuf.hasRemaining()) {
                            System.out.println("Se preparando para escrever proxima mensagem...");
                            writeBuf = null;
                        }
                    }
                }
                keys.clear();
                if (this.send.isEmpty() && writeBuf == null) {
                    if (toWrite) {
                        System.out.println("Interested in read");
                        this.key.interestOps(SelectionKey.OP_READ);
                        toWrite = false;
                    }
                } else {
                    if (!toWrite) {
                        System.out.println("Interested in read and write");
                        this.key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        toWrite = true;
                    }
                }
            }
            selector.close();
            this.sock.close();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e2) {
            e2.printStackTrace();
        }
        this.stop();
        System.out.println("Fechando o BaseClientThread :v");
    }

    protected abstract void receiveMessage(BaseMessage msg);

    public synchronized void sendMessage(BaseMessage message) {
        try {
            System.out.println("Tamanho da queue de escrita do BaseClientThread: " + this.send.size());
            System.out.println("Escrevendo mensagem " + message);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(baos, 10240);
            ObjectOutputStream os = new ObjectOutputStream(gzip);
            os.writeUnshared(message);
            os.flush();
            os.close();
            gzip.flush();
            gzip.close();

            ByteArrayOutputStream baosSize = new ByteArrayOutputStream();
            ObjectOutputStream osSize = new ObjectOutputStream(baosSize);
            osSize.writeInt(baos.size());
            osSize.flush();

            ByteBuffer buf = ByteBuffer.allocate(128);
            buf.put(baosSize.toByteArray());
            buf.rewind();
            while (true) {
                try {
                    this.send.put(buf);
                } catch(InterruptedException e) {
                    continue;
                }
                break;
            }

            buf = ByteBuffer.allocate(baos.size());
            buf.put(baos.toByteArray());
            buf.rewind();

            while (true) {
                try {
                    this.send.put(buf);
                } catch(InterruptedException e) {
                    continue;
                }
                break;
            }
            this.selector.wakeup();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
