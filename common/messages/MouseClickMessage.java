package messages;

/**
 * Created by fernando on 11/04/15.
 */
public class MouseClickMessage implements RobotMessage
{
    private int button;

    public MouseClickMessage(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }
}
