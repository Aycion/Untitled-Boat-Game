package game.assets.sprites;

import engine.*;
import engine.ecs.GameComponent;
import engine.graphics.Animatable;
import engine.ecs.GameObject;
import engine.graphics.SpriteImage;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimatedShipSprite extends SpriteImage implements Animatable {
    private ArrayList<BufferedImage> images;
    private int imageIndex, animationFrames;
    private double animationDuration, animationPercent;

    public AnimatedShipSprite(GameObject parent) throws ResourceNotFound {
        this(parent, EngineCore.assets.getImageList("shipanim.png"));
    }

    public AnimatedShipSprite(GameObject parent, ArrayList<BufferedImage> images) {
        super(parent);

        this.priority = 1;
        this.images = images;

        this.imageIndex = 0;
        this.animationFrames = this.images.size();

        // Duration (in seconds) of animation cycle
        this.animationDuration = 1;
        this.animationPercent = 0;

    }

    @Override
    public BufferedImage getAnimationFrame() {
        if ((this.animationPercent += (EngineCore.clock.getDeltaTime() / this.animationDuration)
        ) * 100 > 100) {
            this.animationPercent = 0;
        }
        this.imageIndex = (int) (this.animationPercent * this.animationFrames);
        return this.images.get(this.imageIndex);
    }

    @Override
    public BufferedImage getFrameAt(int index) throws ArrayIndexOutOfBoundsException {
        return this.images.get(index);
    }

    @Override
    public ArrayList<BufferedImage> getAllFrames() {
        return this.images;
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
    public void logic() {
        this.spriteImg = this.getAnimationFrame();
    }

    public int getWidth() {
        return this.images.get(this.imageIndex).getWidth();
    }

    public int getHeight() {
        return this.images.get(this.imageIndex).getHeight();
    }

}
