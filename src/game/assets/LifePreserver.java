package game.assets;

import engine.EngineCore;
import engine.GameObject;
import engine.Moveable;
import engine.ResourceNotFound;
import engine.colliders.CircleCollider;
import engine.colliders.Collidable;
import engine.colliders.Collider;
import game.assets.sprites.ShipSprite;

import java.awt.geom.AffineTransform;

public class LifePreserver extends GameObject implements Moveable, Collidable {

    // Velocity vars
    double speed;

    private ShipSprite sprite;
    private CircleCollider lifePreserverCollider;

    public LifePreserver(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.speed = 15;

        this.sprite = new ShipSprite(this);
        this.addGraphicsComponent(this.sprite);

        this.lifePreserverCollider = new CircleCollider(
                this,
                10,
                this.sprite.getWidth()
        );
        this.addLogicComponent(this.lifePreserverCollider);
        this.addGraphicsComponent(this.lifePreserverCollider);
    }

    @Override
    public Collider getCollider() {
        return this.lifePreserverCollider;
    }

    @Override
    public void logic() {
        super.logic();
        this.move();
    }

    @Override
    public void move() {

    }
}
