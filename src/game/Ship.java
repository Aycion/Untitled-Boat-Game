package game;

import engine.EngineCore;
import engine.GameObject;
import engine.Moveable;
import engine.graphics.ShipSprite;

import java.awt.geom.AffineTransform;

public class Ship extends GameObject implements Moveable {
    float speed;

    public Ship(EngineCore engine, AffineTransform transform) {
        super(engine, transform);
        this.setCollidable(true);
        this.speed = 5;

        this.addGraphicsComponent(new ShipSprite(this));
    }

    @Override
    public void move() {

    }
}
