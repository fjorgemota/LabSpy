package messages;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Created by fernando on 11/04/15.
 */
public class Screenshot implements BaseMessage
{
    private ImageIcon img;

    public Screenshot(BufferedImage buf) {
        this.img = new ImageIcon(buf);
    }

    public ImageIcon getImage() {
        return img;
    }
}
