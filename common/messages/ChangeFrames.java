package messages;

/**
 * Created by podesta on 26/06/15.
 */
public class ChangeFrames implements BaseMessage {
    private int frames;

    public ChangeFrames(int fps) {
        if (fps <= 0) {
            fps = 1;
        }
        this.frames = fps;
    }

    public int getFrames() {
        return this.frames;
    }
}
