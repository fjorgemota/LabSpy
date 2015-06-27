package messages;

import javax.swing.*;

/**
 * Created by fernando on 11/04/15.
 */
public class InfoMessage implements BaseMessage{
    private String message;

    public InfoMessage(String str) {
        this.message = str;
    }
    public String getMessage() {
        return message;
    }
}
