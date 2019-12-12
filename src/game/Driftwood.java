package game;

import engine.EngineCore;
import engine.GameObject;
import engine.ResourceNotFound;
import engine.colliders.RectangleCollider;
import engine.graphics.DriftwoodSprite;

import java.awt.geom.AffineTransform;

public class Driftwood extends GameObject {

    private DriftwoodSprite sprite;

    public Driftwood(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.sprite = new DriftwoodSprite(this);
        this.addGraphicsComponent(this.sprite);

        RectangleCollider driftwoodCollider = new RectangleCollider(
                this,
                10,
                sprite.getWidth(),
                sprite.getHeight()
        );
        this.addLogicComponent(driftwoodCollider);
        this.addGraphicsComponent(driftwoodCollider);
    }

    @Override
    public void logic() {
        super.logic();
    }
}
