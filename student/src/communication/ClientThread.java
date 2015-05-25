package communication;

import messages.*;
import remote_control.RobotThread;
import remote_control.ScreenshotThread;

import java.awt.*;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * Classe que implementa as threads referentes ao cliente.
 * Um cliente se comunicara com um servidor via envio de
 * mensagens via sockets.
 */
public class ClientThread extends BaseClientThread {
    private ScreenshotThread screenshotThread;
    private RobotThread robotThread;
    private long lastScreenshot;

    public ClientThread(SocketChannel sock, RobotThread robot) {
        super(sock);
        this.robotThread = robot;
        this.lastScreenshot = System.currentTimeMillis()+1000;
        this.screenshotThread = new ScreenshotThread(this, this.robotThread, new Rectangle(400, 300));
    }

    public void stop() {
        super.stop();
        this.screenshotThread.stop();
    }

    @Override
    public synchronized void sendMessage(BaseMessage message) {
        if (message instanceof Screenshot) {
            long now = System.currentTimeMillis();
            System.out.println(now+" > "+this.lastScreenshot);
            if (now > this.lastScreenshot) {
                super.sendMessage(message);
                this.lastScreenshot = now+(1000);
            }
        } else {
            super.sendMessage(message);
        }
    }

    @Override
    protected void receiveMessage(BaseMessage msg) {
        System.out.println("Recebida mensagem");
        if (msg instanceof StartScreenshot) {
            System.out.println("Starting screenshot thread");
            this.screenshotThread.stop();
            this.screenshotThread = new ScreenshotThread(this, this.robotThread, ((StartScreenshot) msg).getRect());
            Thread screenshotThreadReal = new Thread(this.screenshotThread);
            screenshotThreadReal.start();
        } else if (msg instanceof ResizeScreenshot) {
            this.screenshotThread.setRect(((ResizeScreenshot) msg).getRect());
        } else if (msg instanceof StopScreenshot) {
            this.screenshotThread.stop();
        } else if (msg instanceof RobotMessage) {
            this.robotThread.sendMessage((RobotMessage) msg);
        } else {
            System.out.println(msg.toString());
        }
    }
}
