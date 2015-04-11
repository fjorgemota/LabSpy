package messages;

/**
 * Created by fernando on 11/04/15.
 */
public class MouseReleaseMessage implements RobotMessage
{
    private int button;

    public MouseReleaseMessage(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }
}
