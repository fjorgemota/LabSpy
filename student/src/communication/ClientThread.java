package communication;

import jdk.nashorn.internal.ir.Block;
import messages.*;
import commands.OSCommands;
import remote_control.BlockThread;
import remote_control.RobotThread;
import remote_control.ScreenshotThread;

import javax.swing.*;
import java.awt.*;
import java.nio.channels.SocketChannel;

/**
 * Classe que implementa as threads referentes ao cliente.
 * Um cliente se comunicara com um servidor via envio de
 * mensagens via sockets.
 */
public class ClientThread extends BaseClientThread {
    private ScreenshotThread screenshotThread;
    private RobotThread robotThread;
    private BlockThread blockThread;
   //private int frames;
    private Thread runningScreenshotThread;
    private Thread runningBlockThread;

    public ClientThread(SocketChannel sock, RobotThread robot) {
        super(sock);
        this.robotThread = robot;
        this.screenshotThread = new ScreenshotThread(this, this.robotThread, new Rectangle(400, 300));
        this.blockThread = new BlockThread(this.screenshotThread);
    }

    public void stop() {
        super.stop();
        this.screenshotThread.stop();
        this.blockThread.stop();
    }


    @Override
    protected void receiveMessage(BaseMessage msg) {
        if (msg instanceof StartScreenshot) {
            if (this.runningScreenshotThread == null) {
                this.runningScreenshotThread = new Thread(this.screenshotThread);
                this.runningScreenshotThread.setName("screenshotThread");
                this.runningScreenshotThread.start();
            }
            this.screenshotThread.setRect(((StartScreenshot) msg).getRect());
        } else if (msg instanceof InfoMessage) {
            Runnable showMessage = new Runnable() {
                public void run() {
                    //System.out.println("Hello World on " + Thread.currentThread());
                    JOptionPane.showMessageDialog(null, ((InfoMessage) msg).getMessage());
                }
            };
            Thread t = new Thread() {
                public void run() {
                    try {
                        SwingUtilities.invokeAndWait(showMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();

        } else if (msg instanceof ChangeFrames) {
            if (this.runningScreenshotThread == null) {
                this.runningScreenshotThread = new Thread(this.screenshotThread);
                this.runningScreenshotThread.setName("screenshotThread");
                this.runningScreenshotThread.start();
            }
            this.screenshotThread.setFrames(((ChangeFrames) msg).getFrames());
        } else if (msg instanceof ResizeScreenshot) {
            this.screenshotThread.setRect(((ResizeScreenshot) msg).getRect());
        } else if (msg instanceof StopScreenshot) {
            this.screenshotThread.stop();
        } else if (msg instanceof RobotMessage) {
            this.robotThread.sendMessage((RobotMessage) msg);
        } else if (msg instanceof ShutdownMessage) {
            OSCommands shutdown = OSCommands.getInstance();
            shutdown.shutdown();
        } else if (msg instanceof RestartMessage) {
            OSCommands restart = OSCommands.getInstance();
            restart.restart();
        } else if (msg instanceof BlockMessage) {
            if (this.runningScreenshotThread == null) {
                this.runningScreenshotThread = new Thread(this.screenshotThread);
                this.runningScreenshotThread.setName("screenshotThread");
                this.runningScreenshotThread.start();
            }
            if (this.runningBlockThread == null) {
                this.runningBlockThread = new Thread(this.blockThread);
                this.runningBlockThread.setName("blockThread");
                this.runningBlockThread.start();
            }
            this.blockThread.block();
        } else if (msg instanceof UnblockMessage) {
            if (this.runningScreenshotThread == null) {
                this.runningScreenshotThread = new Thread(this.screenshotThread);
                this.runningScreenshotThread.setName("screenshotThread");
                this.runningScreenshotThread.start();
            }
            if (this.runningBlockThread == null) {
                this.runningBlockThread = new Thread(this.blockThread);
                this.runningBlockThread.setName("blockThread");
                this.runningBlockThread.start();
            }
            this.blockThread.unblock();
        } else {
            System.out.println(msg.toString());
        }
    }
}
