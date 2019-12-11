package game;

import engine.EngineCore;
import engine.GameObject;
import engine.Moveable;
import engine.ResourceNotFound;
import engine.colliders.CircleCollider;
import engine.graphics.FortressSprite;

import java.awt.geom.AffineTransform;

public class Fortress extends GameObject {

    private FortressSprite sprite;

    public Fortress(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.sprite = new FortressSprite(this);
        this.addGraphicsComponent(this.sprite);

        CircleCollider fortressCollider = new CircleCollider(this, 10, sprite.getWidth());
        this.addLogicComponent(fortressCollider);
        this.addGraphicsComponent(fortressCollider);
    }

    @Override
    public void logic() {
        super.logic();
    }
}
