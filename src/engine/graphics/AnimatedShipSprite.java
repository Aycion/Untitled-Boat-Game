package engine.graphics;

import engine.*;
import engine.Component;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimatedShipSprite extends Component implements Animatable {
    private ArrayList<BufferedImage> images;
    private int imageIndex, animationFrames;
    private double animationDuration, animationPercent;

    // Creates a CyclingSprite and initializes it with the given List
    public AnimatedShipSprite(GameObject parent) throws ResourceNotFound {
        this(parent, EngineCore.assets.getImageList("shipanim.png"));
    }
    // Creates an empty CyclingSprite
    public AnimatedShipSprite(GameObject parent, ArrayList<BufferedImage> images) {
        super(parent);

        this.priority = 1;
        this.images = images;

        this.imageIndex = 0;
        this.animationFrames = this.images.size();
        this.animationDuration = 1; // Duration (in seconds) of animation cycle

    }


    @Override
    public BufferedImage getAnimationFrame() {
        if ((this.animationPercent += (EngineCore.clock.getDeltaTime() / this.animationDuration)
        ) * 100 > 100) {
            this.animationPercent = 0;
        }
        this.imageIndex = (int) (this.animationPercent * this.animationFrames);
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

}
