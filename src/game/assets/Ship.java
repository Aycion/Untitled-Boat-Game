package game.assets;

import engine.*;
import engine.colliders.CircleCollider;
import engine.colliders.Collidable;
import engine.colliders.Collider;
import engine.colliders.RectangleCollider;
import game.assets.sprites.AnimatedShipSprite;
import game.assets.sprites.LifePreserverSprite;

import java.awt.geom.AffineTransform;

public class Ship extends GameObject implements Moveable, Collidable {

    // Velocity vars
    double speed, maxSpeed,
            acceleration, baseAcceleration,
            turnRate, baseTurnRate;

    double rotAnchorX, rotAnchorY;
    double shipRotAnchorX, shipRotAnchorY;
    double lpRotAnchorX, lpRotAnchorY;

    AnimatedShipSprite shipSprite;
    RectangleCollider shipCollider;

    LifePreserverSprite lpSprite;
    CircleCollider lpCollider;

    Collider activeCollider;

    int shipTimer, lpTimer;

    /**
     * Main constructor for the {@link Ship} class.
     *
     * Instantiate the attributes that define movement. Then create
     * the sprite and add it to the graphics components. Finally,
     * create the {@link Ship}'s collider as a RectangleCollider and
     * add it to the logic and graphics components.
     * @param engine The main game engine object
     * @param transform The transform for this GameObject
     * @throws ResourceNotFound If the sprite resource can't be loaded
     */
    public Ship(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        /*
        Base movement attributes
         */
        this.maxSpeed = 30;         // Maximum translation speed
        this.baseAcceleration = 2;  // Base translation acceleration
        this.baseTurnRate = 2;      // Base turn rate

        // Create the sprites and add them to the graphics components
        this.shipSprite = new AnimatedShipSprite(this);
        this.lpSprite = new LifePreserverSprite(this);

        this.addGraphicsComponent(this.shipSprite);

        // Calculate the rotation anchor points using the
        //  size of the sprite
        this.rotAnchorX = this.shipRotAnchorX = this.shipSprite.getWidth() / 2.0;
        this.rotAnchorY = this.shipRotAnchorY = this.shipSprite.getHeight() / 2.0;

        this.lpRotAnchorX = this.lpSprite.getWidth() / 2.0;
        this.lpRotAnchorY = this.lpSprite.getHeight() / 2.0;

        // Create the collider and add it to the components
        this.shipCollider = new RectangleCollider(
                this,
                10,
                this.shipSprite.getWidth(),
                this.shipSprite.getHeight()
        );

        this.lpCollider = new CircleCollider(
                this,
                10,
                this.lpSprite.getWidth()
        );

        this.activeCollider = this.shipCollider;

        this.addLogicComponent(this.activeCollider);
        this.addGraphicsComponent(this.activeCollider);

        this.shipTimer = 0;
        this.lpTimer = 0;
    }

    public Collider getCollider() {
        return this.activeCollider;
    }

    public double getRotAnchorX() {
        return this.rotAnchorX;
    }

    public double getRotAnchorY() {
        return this.rotAnchorY;
    }

    /**
     * Decelerate the {@link Ship}.
     *
     * Changes the object's translation speed according
     * to its acceleration, adding the acceleration
     * to the speed up to the ship's defined maxSpeed.
     * The acceleration is updated every logic step according
     * to the baseAcceleration and the current deltaTime value;
     */
    protected void accelerate() {
        this.speed = Math.min(
                this.speed + this.acceleration, this.maxSpeed);
    }

    /**
     * Decelerate the {@link Ship}.
     *
     * Changes the object's translation speed according
     * to its acceleration, subtracting the acceleration
     * from the speed down to a minimum of 0. The acceleration
     * is updated every logic step according to the
     * baseAcceleration and the current deltaTime value;
     */
    protected void decelerate() {
        this.speed = Math.max(this.speed - this.acceleration, 0);
    }

    /**
     * Turn the {@link Ship} to the left.
     *
     * Concatenates a negative rotation to the object's
     * deltaTransform corresponding to its turn rate.
     * The rotation is calculated around the set anchor
     * points, which are defined at instantiation
     */
    protected void turnLeft() {
        this.deltaTransform.rotate(
                -this.turnRate,
                this.rotAnchorX, this.rotAnchorY
        );
    }

    /**
     * Turn the {@link Ship} to the right.
     *
     * Concatenates a positive rotation to the object's
     * deltaTransform corresponding to its turn rate.
     * The rotation is calculated around the set anchor
     * points, which are defined at instantiation
     */
    protected void turnRight() {
        this.deltaTransform.rotate(
                this.turnRate,
                this.rotAnchorX, this.rotAnchorY
        );
    }

    protected void replaceCollider(Collider replacement) {
        super.removeLogicComponent(this.activeCollider);
        super.removeGraphicsComponent(this.activeCollider);

        this.activeCollider = replacement;

        super.addLogicComponent(this.activeCollider);
        super.addGraphicsComponent(this.activeCollider);
    }


    @Override
    public void logic() {
        super.logic();

        // Update base movement attributes according to deltaTime
        this.turnRate = (this.baseTurnRate / Math.min(this.speed + 8, this.maxSpeed))
                * EngineCore.clock.getDeltaTime()
                * GameObject.movementMult;
        this.acceleration = (this.baseAcceleration
                * EngineCore.clock.getDeltaTime()
                * GameObject.movementMult);

        // If the player is paying their respects
        if (InputCaptor.bindingActive("F")) {
            if (super.getGraphicsComponent(this.shipSprite) != null) {
                this.lpTimer++;
                this.shipTimer = 0;
            } else {
                this.shipTimer++;
                this.lpTimer = 0;
            }

            if (this.lpTimer > 20) {
                super.removeGraphicsComponent(this.shipSprite);
                super.addGraphicsComponent(this.lpSprite);
                this.rotAnchorX = this.lpRotAnchorX;
                this.rotAnchorY = this.lpRotAnchorY;

                this.replaceCollider(this.lpCollider);
            } else if (this.shipTimer > 20) {
                super.removeGraphicsComponent(this.lpSprite);
                super.addGraphicsComponent(this.shipSprite);
                this.rotAnchorX = this.shipRotAnchorX;
                this.rotAnchorY = this.shipRotAnchorY;

                this.replaceCollider(this.shipCollider);
            }
        } else {
            this.shipTimer = 0;
            this.lpTimer = 0;
        }

        this.move();
    }

    /**
     * Basic move function for all {@link Ship}s.
     *
     * Calculate the velocity with the speed and current
     * timeDelta, then concatenate it to the deltaTransform.
     * Use the negative of velocity to translate it towards the
     * negative y of its own coordinate space, which is forward
     * relative to the sprite.
     */
    @Override
    public void move() {
        double velocity = this.speed * GameObject.movementMult * EngineCore.clock.getDeltaTime();
        this.deltaTransform.translate(0, -velocity);
    }
}
