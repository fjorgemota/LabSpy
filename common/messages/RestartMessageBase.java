package messages;

import commands.OSCommands;

/**
 * Created by paladini on 6/22/15.
 */
public class RestartMessageBase extends BaseCommandMessage {

    protected String getCommand() {
        OSCommands os = OSCommands.getInstance();
        return os.restart();
    }
}
