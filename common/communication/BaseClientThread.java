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
import java.util.zip.*;

/*!
 * Classe que implementa o sistema de comunicação básico entre cliente/servidor.
 */
public abstract class BaseClientThread implements Runnable {
    private SocketChannel sock;
    private boolean stopped;
    private BlockingQueue<ByteBuffer> send;
    private SelectionKey key;
    private Selector selector;
    private boolean toWrite;

    public BaseClientThread(SocketChannel sock) {
        this.sock = sock;
        this.stopped = false;
        this.key = null;
        this.toWrite = false;
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.send = new LinkedBlockingQueue<>(100);
    }

    public void stop() {
        this.stopped = true;
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
                                Inflater inflater = new Inflater();
                                InflaterInputStream descompressor = new InflaterInputStream(bias, inflater, 102400);
                                ObjectInputStream interpreter = new ObjectInputStream(descompressor);
                                BaseMessage message = (BaseMessage) interpreter.readUnshared();
                                this.receiveMessage(message);
                                readBuf = null;
                                readSize = -1;
                                descompressor.close();
                                inflater.end();
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
                        if (!writeBuf.hasRemaining()) {
                            writeBuf = null;
                        }
                    }
                }
                keys.clear();
                synchronized (this) {
                    if (this.send.isEmpty() && writeBuf == null) {
                        if (this.toWrite) {
                            this.key.interestOps(SelectionKey.OP_READ);
                            this.toWrite = false;
                        }
                    } else {
                        if (!this.toWrite) {
                            this.key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            this.toWrite = true;
                        }
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
    }

    protected abstract void receiveMessage(BaseMessage msg);

    public synchronized void sendMessage(BaseMessage message) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Deflater deflater = new Deflater();
            deflater.setLevel(5);
            deflater.setStrategy(Deflater.FILTERED);
            DeflaterOutputStream compressor = new DeflaterOutputStream(baos, deflater, 102400);
            ObjectOutputStream os = new ObjectOutputStream(compressor);
            os.writeUnshared(message);
            os.flush();
            os.close();
            compressor.flush();
            compressor.close();
            deflater.end();


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
            if (!this.toWrite) {
                this.selector.wakeup();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
