package messages;

/**
 * Created by fernando on 11/04/15.
 */
public class MouseMoveMessage implements RobotMessage
{
    private int x;
    private int y;
    private int width;
    private int height;

    public MouseMoveMessage(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }



    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
