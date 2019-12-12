package game;

import engine.*;
import engine.colliders.RectangleCollider;
import engine.graphics.ShipSprite;

import java.awt.geom.AffineTransform;

public class Ship extends GameObject implements Moveable {

    // Velocity vars
    double speed, maxSpeed, acceleration, turnRate, baseTurnRate;
    Direction direction;

    private ShipSprite sprite;
    double rotAnchorX, rotAnchorY;

    public Ship(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.speed = 0;
        this.maxSpeed = 15;
        this.acceleration = 2;
        this.turnRate = this.baseTurnRate = 1;

        this.sprite = new ShipSprite(this);
        this.addGraphicsComponent(this.sprite);

        this.rotAnchorX = this.sprite.getWidth() / 2.0;
        this.rotAnchorY = 0.6 * this.sprite.getHeight();

        RectangleCollider shipCollider = new RectangleCollider(
                this,
                10,
                sprite.getWidth(),
                sprite.getHeight()
        );
        this.addLogicComponent(shipCollider);
        this.addGraphicsComponent(shipCollider);
    }

    protected void accelerate() {
        this.speed = Math.min(
                this.speed +
                        (this.acceleration * EngineCore.clock.getDeltaTime() * GameObject.movementMult),
                this.maxSpeed
        );
    }

    protected void decelerate() {
        this.speed = Math.max(
                this.speed -
                        (this.acceleration * EngineCore.clock.getDeltaTime() * GameObject.movementMult),
                0
        );
    }

    protected void turnLeft() {
        this.deltaTransform.rotate(
                -this.turnRate,
                this.rotAnchorX, this.rotAnchorY
        );
    }

    protected void turnRight() {
        this.deltaTransform.rotate(
                this.turnRate,
                this.rotAnchorX, this.rotAnchorY
        );
    }


    @Override
    public void logic() {
        super.logic();

        this.turnRate = (this.baseTurnRate / Math.min(this.speed + 8, this.maxSpeed))
                * EngineCore.clock.getDeltaTime() * GameObject.movementMult;
        this.move();
    }

    @Override
    public void move() {
        double velocity = this.speed * GameObject.movementMult * EngineCore.clock.getDeltaTime();
        this.deltaTransform.translate(0, -velocity);
    }
}
