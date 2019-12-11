package game;

import engine.EngineCore;
import engine.GameObject;
import engine.Moveable;
import engine.ResourceNotFound;
import engine.colliders.RectangleCollider;
import engine.graphics.ShipSprite;

import java.awt.geom.AffineTransform;

public class Ship extends GameObject implements Moveable {

    // Velocity vars
    double speed;
    Direction direction;

    private ShipSprite sprite;

    public Ship(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.speed = 15;

        this.sprite = new ShipSprite(this);
        this.addGraphicsComponent(this.sprite);

        RectangleCollider shipCollider = new RectangleCollider(
                this,
                10,
                sprite.getWidth(),
                sprite.getHeight()
        );
        this.addLogicComponent(shipCollider);
        this.addGraphicsComponent(shipCollider);
    }

    protected void changeDirection(Direction newDir) {
        if (this.direction != newDir) {

            // Calculate the angle of rotation based on the
            //  previous direction and the new one, then add
            //  the rotation to the object's deltaTransform
            this.deltaTransform.rotate(
                    Math.toRadians(this.direction.getAngleDiff(newDir)),
                    this.sprite.getWidth() / 2.0,
                    0.6 * this.sprite.getHeight()
            );

            this.direction = newDir;
        }
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
