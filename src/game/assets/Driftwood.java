package game.assets;

import engine.EngineCore;
import engine.GameObject;
import engine.ResourceNotFound;
import engine.colliders.Collidable;
import engine.colliders.Collider;
import engine.colliders.RectangleCollider;
import game.assets.sprites.DriftwoodSprite;

import java.awt.geom.AffineTransform;

public class Driftwood extends GameObject implements Collidable {

    private DriftwoodSprite sprite;
    private Collider driftwoodCollider;

    public Driftwood(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.sprite = new DriftwoodSprite(this);
        this.addGraphicsComponent(this.sprite);

        this.driftwoodCollider = new RectangleCollider(
                this,
                10,
                this.sprite.getWidth(),
                this.sprite.getHeight()
        );
        this.addLogicComponent(this.driftwoodCollider);
        this.addGraphicsComponent(this.driftwoodCollider);
    }

    @Override
    public Collider getCollider() {
        return this.driftwoodCollider;
    }

}
