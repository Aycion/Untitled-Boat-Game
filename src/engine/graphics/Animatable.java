package engine.graphics;

import engine.ecs.GameComponent;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface Animatable extends GameComponent.Drawable, GameComponent.Updatable {
    BufferedImage getAnimationFrame();
    BufferedImage getFrameAt(int index) throws ArrayIndexOutOfBoundsException;
    ArrayList<BufferedImage> getAllFrames();
}
