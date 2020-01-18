package engine.graphics;

import engine.ecs.GameComponent;
import engine.ecs.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class SpriteImage extends GameComponent implements GameComponent.Drawable {
    protected BufferedImage spriteImg;

    public SpriteImage(GameObject parent, BufferedImage image) {
        super(parent);

        this.priority = 1;
        this.spriteImg = image;

    }

    public SpriteImage(GameObject parent) {
        super(parent);
    }

    public int getWidth() {
        return this.spriteImg.getWidth();
    }

    public int getHeight() {
        return this.spriteImg.getHeight();
    }

    @Override
    public AffineTransform getLocalTransform() {
        return this.localTransform;
    }

    @Override
    public AffineTransform getGlobalTransform() {
        AffineTransform gT = new AffineTransform(this.parent.getTransform());
        gT.concatenate(this.localTransform);
        return gT;
    }

    public BufferedImage getSpriteImg() {
        return this.spriteImg;
    }

    @Override
    public void graphic(Graphics2D g) {
        g.drawImage(
                this.getSpriteImg(),
                this.getGlobalTransform(),
                null
        );
    }
}
