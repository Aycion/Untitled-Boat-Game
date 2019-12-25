package game;

import engine.EngineCore;
import engine.GameObject;
import engine.Moveable;
import engine.ResourceNotFound;
import engine.colliders.CircleCollider;
import engine.colliders.Collidable;
import engine.colliders.Collider;
import engine.graphics.FortressSprite;

import java.awt.geom.AffineTransform;

public class Fortress extends GameObject implements Collidable {

    private FortressSprite sprite;
    private CircleCollider fortressCollider;

    public Fortress(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.sprite = new FortressSprite(this);
        this.addGraphicsComponent(this.sprite);

        this.fortressCollider = new CircleCollider(this, 10, sprite.getWidth());
        this.addLogicComponent(this.fortressCollider);
        this.addGraphicsComponent(this.fortressCollider);
    }

    @Override
    public Collider getCollider() {
        return this.fortressCollider;
    }

    @Override
    public void logic() {
        super.logic();
    }

}
