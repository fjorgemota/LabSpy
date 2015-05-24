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
    private boolean stopped;
    private BlockingQueue<ByteBuffer> send;

    public BaseClientThread(SocketChannel sock) {
        this.sock = sock;
        this.stopped = false;
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
            Selector selector = Selector.open();
            this.sock.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            ByteBuffer readBuf = null;
            ByteBuffer writeBuf = null;

            int readSize = -1;
            while (!this.stopped) {
                int num = selector.select();
                if (num == 0) {
                    continue;
                }
                if (this.sock.socket().isClosed()) {
                    this.stop();
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                for (SelectionKey key: keys) {
                    if (key.isReadable()) {
                        if (readBuf == null) {
                            readBuf = ByteBuffer.allocate(128);
                        }
                        if (this.sock.read(readBuf) == -1) {
                            this.stop();
                        }
                        if (readBuf.remaining() == 0) { // If there's no remaining buffer
                            readBuf.rewind();
                            ByteArrayInputStream bias = new ByteArrayInputStream(readBuf.array());
                            ObjectInputStream interpreter = new ObjectInputStream(bias);
                            if (readSize == -1) {
                                readSize = interpreter.readInt();
                                readBuf = ByteBuffer.allocate(readSize);
                            } else {
                                BaseMessage message = (BaseMessage) interpreter.readUnshared();
                                System.out.println("Recebida mensagem "+message);
                                this.receiveMessage(message);
                                System.out.println("Processada mensagem recebida " + message);
                                readBuf = null;
                                readSize = -1;
                            }
                        }
                    }
                    if (key.isWritable() && !this.send.isEmpty()) {
                        if (writeBuf == null) {
                            System.out.println("Removendo mensagem da lista de mensagens enviadas");
                            writeBuf = this.send.poll();
                        }
                        if (writeBuf == null) {
                            continue;
                        }
                        this.sock.write(writeBuf);
                        if (writeBuf.remaining() == 0) {
                            System.out.println("Se preparando para escrever proxima mensagem...");
                            writeBuf = null;
                        }
                    }
                }
                keys.clear();
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
            System.out.println("Tamanho da queue de escrita do BaseClientThread: "+this.send.size());
            System.out.println("Escrevendo mensagem "+message);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(baos);
            os.writeUnshared(message);
            os.flush();

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
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
