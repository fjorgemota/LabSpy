package messages;

import java.awt.*;

/**
 * Created by fernando on 11/04/15.
 */
public class ScreenshotRequest implements RobotMessage {
    private Rectangle rect;

    public ScreenshotRequest(Rectangle rect) {
        this.rect = rect;
    }

    public Rectangle getRect() {
        return rect;
    }
}
