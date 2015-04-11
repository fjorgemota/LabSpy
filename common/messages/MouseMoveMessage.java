package messages;

/**
 * Created by fernando on 11/04/15.
 */
public class MouseMoveMessage implements RobotMessage
{
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
