package game;

import engine.*;
import engine.colliders.CircleCollider;
import engine.colliders.Collider;
import engine.colliders.RectangleCollider;
import engine.graphics.CyclingSprite;
import engine.graphics.LifePreserverSprite;
import engine.graphics.ShipSprite;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Ship extends GameObject implements Moveable {

    // Velocity vars
    double speed, maxSpeed,
            acceleration, baseAcceleration,
            turnRate, baseTurnRate;

    double rotAnchorX, rotAnchorY;
    double shipRotAnchorX, shipRotAnchorY;
    double lpRotAnchorX, lpRotAnchorY;

    CyclingSprite shipSprite;
    RectangleCollider shipCollider;
    LifePreserverSprite lpSprite;
    CircleCollider lpCollider;

    int shipTimer, lpTimer;

    /**
     * Main constructor for the {@link Ship} class.
     *
     * Instantiate the attributes that define movement. Then create
     * the sprite and add it to the graphics components. Finally,
     * create the {@link Ship}'s collider as a RectangleCollider and
     * add it to the logic and graphics components.
     * @param engine
     * @param transform
     * @throws ResourceNotFound
     */
    public Ship(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        /*
        Base movement attributes
         */
        this.maxSpeed = 30;         // Maximum translation speed
        this.baseAcceleration = 2;  // Base translation acceleration
        this.baseTurnRate = 2;      // Base turn rate

        ArrayList<BufferedImage> shipSpriteList = new ArrayList<>();
        shipSpriteList.add((BufferedImage) EngineCore.assets.getImage("ship1.png", 0));
        shipSpriteList.add((BufferedImage) EngineCore.assets.getImage("ship2.png", 0));
        shipSpriteList.add((BufferedImage) EngineCore.assets.getImage("ship3.png", 0));
        shipSpriteList.add((BufferedImage) EngineCore.assets.getImage("ship4.png", 0));
        shipSpriteList.add((BufferedImage) EngineCore.assets.getImage("ship3.png", 0));
        shipSpriteList.add((BufferedImage) EngineCore.assets.getImage("ship2.png", 0));
        shipSpriteList.add((BufferedImage) EngineCore.assets.getImage("ship1.png", 0));

        // Create the sprites and add them to the graphics components
        shipSprite = new CyclingSprite(this, 1, shipSpriteList, 40);
        lpSprite = new LifePreserverSprite(this);

        this.addGraphicsComponent(shipSprite);

        // Calculate the rotation anchor points using the
        //  size of the sprite
        this.rotAnchorX = this.shipRotAnchorX = shipSprite.getWidth() / 2.0;
        this.rotAnchorY = this.shipRotAnchorY = shipSprite.getHeight() / 2.0;

        this.lpRotAnchorX = lpSprite.getWidth() / 2.0;
        this.lpRotAnchorY = lpSprite.getHeight() / 2.0;

        // Create the collider and add it to the components
        shipCollider = new RectangleCollider(
                this,
                10,
                shipSprite.getWidth(),
                shipSprite.getHeight()
        );

        lpCollider = new CircleCollider(
                this,
                10,
                lpSprite.getWidth()
        );

        this.addLogicComponent(shipCollider);
        this.addGraphicsComponent(shipCollider);

        shipTimer = 0;
        lpTimer = 0;
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
            if (super.getGraphicsComponent(shipSprite) != null) {
                lpTimer++;
                shipTimer = 0;
            } else {
                shipTimer++;
                lpTimer = 0;
            }

            if (lpTimer > 20) {
                super.removeGraphicsComponent(shipSprite);
                super.removeLogicComponent(shipCollider);
                super.removeGraphicsComponent(shipCollider);
                super.addGraphicsComponent(lpSprite);
                super.addLogicComponent(lpCollider);
                super.addGraphicsComponent(lpCollider);
                rotAnchorX = lpRotAnchorX;
                rotAnchorY = lpRotAnchorY;
            } else if (shipTimer > 20) {
                super.removeGraphicsComponent(lpSprite);
                super.removeLogicComponent(lpCollider);
                super.removeGraphicsComponent(lpCollider);
                super.addGraphicsComponent(shipSprite);
                super.addLogicComponent(shipCollider);
                super.addGraphicsComponent(shipCollider);
                rotAnchorX = shipRotAnchorX;
                rotAnchorY = shipRotAnchorY;
            }
        } else {
            shipTimer = 0;
            lpTimer = 0;
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
