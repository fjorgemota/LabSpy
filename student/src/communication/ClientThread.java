package communication;

import messages.BaseMessage;
import messages.RobotMessage;
import messages.StartScreenshot;
import messages.StopScreenshot;
import remote_control.RobotThread;
import remote_control.ScreenshotThread;

import java.net.Socket;

/**
 * Classe que implementa as threads referentes ao cliente.
 * Um cliente se comunicara com um servidor via envio de
 * mensagens via sockets.
 */
public class ClientThread extends BaseClientThread {
    private ScreenshotThread screenshotThread;
    private RobotThread robotThread;

    public ClientThread(Socket sock, RobotThread robot) {
        super(sock);
        this.robotThread = robot;
        this.screenshotThread = new ScreenshotThread(this, this.robotThread);
    }

    @Override
    protected void receiveMessage(BaseMessage msg) {
        if (msg instanceof StartScreenshot) {
            System.out.println("Starting screenshot thread");
            this.screenshotThread.stop();
            this.screenshotThread = new ScreenshotThread(this, this.robotThread);
            Thread thread = new Thread(this.screenshotThread);
            thread.start();
        } else if (msg instanceof StopScreenshot) {
            System.out.println("Stopping screenshot thread");
            this.screenshotThread.stop();
        } else if (msg instanceof RobotMessage) {
            System.out.println("Sending message to robot..");
            this.robotThread.sendMessage((RobotMessage) msg);
        } else {
            System.out.println("Received unknwon type of message..");
            System.out.println(msg.toString());
        }
    }
}