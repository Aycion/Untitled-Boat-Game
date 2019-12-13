package engine.graphics;

import engine.Component;
import engine.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class CyclingSprite extends Component implements Animatable {
    private ArrayList<BufferedImage> images;
    private int imageIndex, frameCounter, framesPerAnimation;

    // Creates an empty CyclingSprite
    public CyclingSprite(GameObject parent, int priority, int framesPerAnimation) {
        super(parent);
        super.priority = priority;
        images = new ArrayList<>();
        this.imageIndex = 0;
        this.frameCounter = 0;
        this.framesPerAnimation = framesPerAnimation;
    }

    // Creates a CyclingSprite and initializes it with the given List
    public CyclingSprite(GameObject parent, int priority, List<BufferedImage> images, int framesPerAnimation) {
        this(parent, priority, framesPerAnimation);
        this.images.addAll(images);
    }

    public boolean addImage(BufferedImage image) {
        return this.images.add(image);
    }

    @Override
    public BufferedImage getAnimationFrame() {
        if (this.frameCounter++ > this.framesPerAnimation) {
            this.incrementImageIndex();
            this.frameCounter = 0;
        }

        return images.get(imageIndex);
    }

    @Override
    public AffineTransform getLocalTransform() {
        return super.localTransform;
    }

    @Override
    public AffineTransform getGlobalTransform() {
        AffineTransform gT = new AffineTransform(this.parent.getTransform());
        gT.concatenate(this.localTransform);
        return gT;
    }

    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);
        g.drawImage(
                this.getAnimationFrame(),
                this.getGlobalTransform(),
                null
        );
    }

    public int getWidth() {
        return this.images.get(imageIndex).getWidth();
    }

    public int getHeight() {
        return this.images.get(imageIndex).getHeight();
    }

    private void incrementImageIndex() {
        this.imageIndex = (this.imageIndex + 1) % images.size();
    }
}
