package messages;

/**
 * Created by fernando on 11/04/15.
 */
public class MousePressMessage implements RobotMessage
{
    private int button;

    public MousePressMessage(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }

}
