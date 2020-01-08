package game.assets.sprites;

import engine.EngineCore;
import engine.GameObject;
import engine.ResourceNotFound;
import engine.graphics.SpriteImage;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class FortressSprite extends SpriteImage {


    public FortressSprite(GameObject object, BufferedImage img)  {
        super(object, img);

        this.priority = 1;
        this.localTransform = new AffineTransform();

    }

    public FortressSprite(GameObject parent) throws ResourceNotFound {
        this(parent, EngineCore.assets.getImage("fortress.png", 0));
    }
}