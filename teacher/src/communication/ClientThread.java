package communication;

import others.Computer;
import messages.BaseMessage;
import messages.InfoMessage;
import messages.Screenshot;
import messages.ChatMessage;

import java.nio.channels.SocketChannel;

/*!
 * Classe que implementa as remote_control referentes ao cliente.
 * Um cliente se comunicara com um servidor via envio de 
 * mensagens via sockets.
 */
public class ClientThread extends BaseClientThread {
    private Screenshot lastScreenshot;
    private InfoMessage info;
    private Computer computer;
    private ChatTeacher chat;

    public ClientThread(SocketChannel sock, Computer computer) {
        super(sock);
        this.computer = computer;
        this.info = null;
        this.lastScreenshot = null;
    }

    protected void receiveMessage(BaseMessage msg) {
        if (msg instanceof Screenshot) {
            this.lastScreenshot = (Screenshot) msg;
        } else if (msg instanceof InfoMessage) {
            this.info = (InfoMessage) msg;
            this.chat = new ChatTeacher();
            this.chat.receiveMessage(this.info.getMessage());
            this.chat.sendMessage(ClientThread.this);
        } else if (msg instanceof ChatMessage) {
            ChatMessage mess = (ChatMessage) msg;
            this.chat.receiveMessage(mess.getMessage());
            this.chat.sendMessage(ClientThread.this);
        }
    }

    public Screenshot getLastScreenshot() {
        return this.lastScreenshot;
    }

    public InfoMessage getInfo() {
        return this.info;
    }

    public Computer getComputer() {
        return computer;
    }
}
