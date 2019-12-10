package game;

import engine.EngineCore;
import engine.GameObject;
import engine.Moveable;

import java.awt.geom.AffineTransform;

public class Ship extends GameObject implements Moveable {
    public Ship(EngineCore engine, AffineTransform transform) {
        super(engine, transform);
        this.setCollidable(true);
    }

    @Override
    public void move() {

    }
}
