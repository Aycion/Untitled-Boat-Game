package engine.graphics;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface Animatable {
    BufferedImage getAnimationFrame();
    BufferedImage getFrameAt(int index) throws ArrayIndexOutOfBoundsException;
    ArrayList<BufferedImage> getAllFrames();
}
