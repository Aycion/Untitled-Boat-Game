package game.assets;

import engine.EngineCore;
import engine.ecs.GameObject;
import engine.ResourceNotFound;
import engine.colliders.Collidable;
import engine.colliders.Collider;
import engine.colliders.RectangleCollider;
import game.assets.sprites.DriftwoodSprite;

import java.awt.geom.AffineTransform;

public class Driftwood extends GameObject implements Collidable {

    private Collider driftwoodCollider;

    public Driftwood(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        DriftwoodSprite sprite = new DriftwoodSprite(this);
        this.attachComponent(sprite);

        this.driftwoodCollider = new RectangleCollider(
                this,
                10,
                sprite.getWidth(),
                sprite.getHeight()
        );
        this.attachComponent(this.driftwoodCollider);
    }

    @Override
    public Collider getCollider() {
        return this.driftwoodCollider;
    }

}
