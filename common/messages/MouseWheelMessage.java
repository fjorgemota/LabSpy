package messages;

/**
 * Created by fernando on 11/04/15.
 */
public class MouseWheelMessage implements RobotMessage
{
    private int wheel;

    public MouseWheelMessage(int wheel) {
        this.wheel = wheel;
    }

    public int getWheel() {
        return wheel;
    }
}
