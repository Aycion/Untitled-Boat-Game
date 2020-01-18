package game.assets;

import engine.EngineCore;
import engine.ecs.GameObject;
import engine.ResourceNotFound;
import engine.colliders.CircleCollider;
import engine.colliders.Collidable;
import engine.colliders.Collider;
import game.assets.sprites.FortressSprite;

import java.awt.geom.AffineTransform;

public class Fortress extends GameObject implements Collidable {

    private FortressSprite sprite;
    private CircleCollider fortressCollider;

    public Fortress(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.sprite = new FortressSprite(this);
        this.attachComponent(this.sprite);

        this.fortressCollider = new CircleCollider(this, 10, this.sprite.getWidth());

        this.attachComponent(this.fortressCollider);
    }

    @Override
    public Collider getCollider() {
        return this.fortressCollider;
    }


}
