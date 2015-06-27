package messages;

/**
 * Created by podesta on 26/06/15.
 */
public class ChangeFrames implements BaseMessage {
    private int frames;

    public ChangeFrames(int fps) {
        this.frames = fps;
    }


    public void setFrames(int fps) {
        this.frames = fps;
    }

    public int getFrames() {
        return this.frames;
    }
}
