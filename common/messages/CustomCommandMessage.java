package messages;

/**
 * Created by paladini on 6/24/15.
 */
public class CustomCommandMessage extends BaseCommandMessage {
    private String command;
    public CustomCommandMessage(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
