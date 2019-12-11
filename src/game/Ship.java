package game;

import engine.EngineCore;
import engine.GameObject;
import engine.Moveable;
import engine.ResourceNotFound;
import engine.graphics.ShipSprite;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class Ship extends GameObject implements Moveable {
    float speed;
    Direction direction;
    private ShipSprite sprite;

    public Ship(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);
        this.setCollidable(true);
        this.speed = 5;
        this.sprite = new ShipSprite(this);
        this.addGraphicsComponent(this.sprite);
    }

    protected void changeDirection(Direction newDir) {
        if (this.direction != (this.direction = newDir)) {

            AffineTransform tempT = new AffineTransform();
            tempT.setToTranslation(
                    this.transform.getTranslateX(),
                    this.transform.getTranslateY()
            );

            tempT.quadrantRotate(
                    this.direction.rotation,
                    this.sprite.getWidth() / 2.0,
                    this.sprite.getHeight() / 2.0
            );

            try {
                tempT.invert();
            } catch (NoninvertibleTransformException e) {
                e.printStackTrace();
            }
            this.transform = (tempT);
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
