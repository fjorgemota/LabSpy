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
    private Thread runningScreenshotThread;

    public ClientThread(SocketChannel sock, RobotThread robot) {
        super(sock);
        this.robotThread = robot;
        this.screenshotThread = new ScreenshotThread(this, this.robotThread, new Rectangle(400, 300));
    }

    public void stop() {
        super.stop();
        this.screenshotThread.stop();
    }


    @Override
    protected void receiveMessage(BaseMessage msg) {
        if (msg instanceof StartScreenshot) {
            if (this.runningScreenshotThread == null) {
                this.runningScreenshotThread = new Thread(this.screenshotThread);
                this.runningScreenshotThread.setName("screenshotThread");
                this.runningScreenshotThread.start();
            }
            this.screenshotThread.setRect(((StartScreenshot)msg).getRect());
        } else if (msg instanceof ResizeScreenshot) {
            this.screenshotThread.setRect(((ResizeScreenshot) msg).getRect());
        } else if (msg instanceof StopScreenshot) {
            this.screenshotThread.stop();
        } else if (msg instanceof RobotMessage) {
            this.robotThread.sendMessage((RobotMessage) msg);
        } else if (msg instanceof CommandMessage) {
            ((CommandMessage) msg).execute();
        } else {
            System.out.println(msg.toString());
        }
    }
}
