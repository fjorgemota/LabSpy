package messages;

/**
 * Created by fernando on 11/04/15.
 */
public class KeyPressMessage implements RobotMessage {
    private int keyCode;

    public KeyPressMessage(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
