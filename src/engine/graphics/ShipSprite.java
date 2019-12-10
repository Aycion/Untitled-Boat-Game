package engine.graphics;

import engine.Component;
import engine.EngineCore;
import engine.GameObject;
import engine.ResourceNotFound;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ShipSprite extends Component implements Animatable {

    private static BufferedImage spriteImg;

    static {
        try {
            spriteImg = (BufferedImage)
                        EngineCore.assets.getImage("ship1.png", 0);
        } catch (ResourceNotFound resourceNotFound) {
            resourceNotFound.printStackTrace();
        }
    }

    public ShipSprite(GameObject object) {
        super(object);
        this.localTransform = new AffineTransform(object.getTransform());

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

    @Override
    public BufferedImage getAnimationFrame() {
        return spriteImg;
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
}
