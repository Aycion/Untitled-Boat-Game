package game.assets.sprites;

import engine.EngineCore;
import engine.ecs.GameObject;
import engine.ResourceNotFound;
import engine.graphics.SpriteImage;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class DriftwoodSprite extends SpriteImage {


    public DriftwoodSprite(GameObject parent, BufferedImage img) {
        super(parent, img);

        this.priority = 1;
        this.localTransform = new AffineTransform();
    }

    public DriftwoodSprite(GameObject parent) throws ResourceNotFound {
        this(parent, EngineCore.assets.getImage("driftwood.png", 0));
    }

}
