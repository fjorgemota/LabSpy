package messages;

/**
 * Created by paladini on 6/22/15.
 */
public class RestartMessageBase extends BaseCommandMessage {

    protected String getCommand() {
        return "sudo reboot";
    }
}
