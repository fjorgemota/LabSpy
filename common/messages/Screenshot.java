package messages;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectStreamException;

/**
 * Created by fernando on 11/04/15.
 */
public class Screenshot implements BaseMessage
{
    private BufferedImage img;

    public Screenshot(BufferedImage img) {
        this.img = img;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        ImageIO.setUseCache(false);
        ImageIO.write(img, "png", out);
    }
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.img = ImageIO.read(in);
    }

    private void readObjectNoData() throws ObjectStreamException {
        System.err.println("Error on deserializing Screenshot");
        this.img = null;
    }

    public BufferedImage getImage() {
        return this.img;
    }
}
