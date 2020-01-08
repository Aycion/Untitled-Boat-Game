package game.assets.sprites;

import engine.EngineCore;
import engine.GameObject;
import engine.ResourceNotFound;
import engine.graphics.SpriteImage;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ShipSprite extends SpriteImage {

    public ShipSprite(GameObject parent, BufferedImage img) {
        super(parent, img);

        this.priority = 1;
        this.localTransform = new AffineTransform();
    }

    public ShipSprite(GameObject parent) throws ResourceNotFound {
        this(parent, EngineCore.assets.getImage("ship.png", 0));
    }
}
