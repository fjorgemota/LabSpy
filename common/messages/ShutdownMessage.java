package messages;

import commands.OSCommands;

/**
 * Created by paladini on 6/22/15.
 */
public class ShutdownMessage extends BaseCommandMessage {
    public void sendShutdown() {
    	OSCommands os = OSCommands.getInstance();
    	os.shutdown();
    }
}
