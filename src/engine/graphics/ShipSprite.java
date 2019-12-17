package engine.graphics;

import engine.Component;
import engine.EngineCore;
import engine.GameObject;
import engine.ResourceNotFound;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ShipSprite extends Component implements StaticImage {

    private BufferedImage spriteImg;

    public ShipSprite(GameObject object) throws ResourceNotFound {
        super(object);
        this.priority = 1;
        spriteImg = (BufferedImage)
                EngineCore.assets.getImage("ship.png", 0);
        this.localTransform = new AffineTransform();

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

    public int getWidth() {
        return this.spriteImg.getWidth();
    }

    public int getHeight() {
        return this.spriteImg.getHeight();
    }

    @Override
    public BufferedImage getImage() {
        return this.spriteImg;
    }

    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);
        g.drawImage(
                this.getImage(),
                this.getGlobalTransform(),
                null
        );
    }
}
