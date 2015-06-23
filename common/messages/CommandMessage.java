package messages;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by paladini on 6/22/15.
 */
public abstract class CommandMessage implements BaseMessage {

    protected abstract String getCommand();

    public void execute() {
        Process p;
        try {
            p = Runtime.getRuntime().exec(getCommand());
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
