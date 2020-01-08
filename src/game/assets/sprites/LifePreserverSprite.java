package game.assets.sprites;

import engine.EngineCore;
import engine.GameObject;
import engine.ResourceNotFound;
import engine.graphics.SpriteImage;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class LifePreserverSprite extends SpriteImage {


    public LifePreserverSprite(GameObject object, BufferedImage img) {
        super(object, img);
        this.priority = 0;
        this.localTransform = new AffineTransform();

    }

    public LifePreserverSprite(GameObject parent) throws ResourceNotFound {
        this(parent, EngineCore.assets.getImage("lifepreserver.png", 0));
    }
}