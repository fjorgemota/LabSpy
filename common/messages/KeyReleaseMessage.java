package messages;

/**
 * Created by fernando on 11/04/15.
 */
public class KeyReleaseMessage implements RobotMessage {
    private int keyCode;

    public KeyReleaseMessage(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
