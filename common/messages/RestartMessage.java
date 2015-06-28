package messages;

import commands.OSCommands;

/**
 * Created by paladini on 6/22/15.
 */
public class RestartMessage extends BaseCommandMessage {
    public void sendRestart() {
    	OSCommands os = OSCommands.getInstance();
    	os.shutdown();
    }
}
