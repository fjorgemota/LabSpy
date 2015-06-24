package communication;

import messages.*;
import remote_control.RobotThread;
import remote_control.ScreenshotThread;

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
            this.screenshotThread.stop();
            this.screenshotThread = new ScreenshotThread(this, this.robotThread, ((StartScreenshot) msg).getRect());
            Thread screenshotThreadReal = new Thread(this.screenshotThread);
            screenshotThreadReal.setName("screenshotThread");
            screenshotThreadReal.start();
        } else if (msg instanceof ResizeScreenshot) {
            this.screenshotThread.setRect(((ResizeScreenshot) msg).getRect());
        } else if (msg instanceof StopScreenshot) {
            this.screenshotThread.stop();
        } else if (msg instanceof RobotMessage) {
            this.robotThread.sendMessage((RobotMessage) msg);
        } else if (msg instanceof BaseCommandMessage) {
            ((BaseCommandMessage) msg).execute();
        } else {
            System.out.println(msg.toString());
        }
    }
}
