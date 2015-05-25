package messages;

import java.awt.*;

/**
 * Created by fernando on 11/04/15.
 */
public class ResizeScreenshot implements BaseMessage {
    private Rectangle rect;

    public ResizeScreenshot(Rectangle rect) {
        this.rect = rect;
    }

    public Rectangle getRect() {
        return rect;
    }
}
