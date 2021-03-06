package engine;

import java.awt.geom.AffineTransform;

public class Projectile extends GameObject implements Moveable {

    static int speed = 5;

    public Projectile(EngineCore engine, AffineTransform transform) {
        super(engine, transform);
        this.transform.scale(.5, .5);
    }

    @Override
    public void move() {
        double vel = speed * 100 * EngineCore.clock.getDeltaTime();
        this.transform.translate(0, vel);
    }

    @Override
    public void logic() {
        super.logic();
        this.move();
    }
}
