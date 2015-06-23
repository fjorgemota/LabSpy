package messages;

/**
 * Created by paladini on 6/22/15.
 */
public class RestartMessage extends CommandMessage {

    protected String getCommand() {
        return "sudo reboot";
    }
}
