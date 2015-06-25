package messages;

/**
 * Created by paladini on 6/22/15.
 */
public class ShutdownMessageBase extends BaseCommandMessage {
    protected String getCommand() {
        return "sudo poweroff";
    }
}
