package messages;

import java.awt.*;

/**
 * Created by fernando on 11/04/15.
 */
public class StartScreenshot implements BaseMessage {
    private Rectangle rect;

    public StartScreenshot(Rectangle rect) {
        this.rect = rect;
    }

    public Rectangle getRect() {
        return rect;
    }
}
