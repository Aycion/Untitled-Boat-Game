package game;

import engine.EngineCore;
import engine.GameObject;
import engine.Moveable;

import java.awt.geom.AffineTransform;

public class Ship extends GameObject implements Moveable {
    float speed;

    public Ship(EngineCore engine, AffineTransform transform) {
        super(engine, transform);
        this.setCollidable(true);
        this.speed = 0;

    }

    @Override
    public void move() {

    }
}
