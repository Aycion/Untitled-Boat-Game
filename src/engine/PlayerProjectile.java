package engine;

import game.Teardrop;

import java.awt.geom.AffineTransform;

public class PlayerProjectile extends GameObject implements Moveable {

    static int speed = 5;

    Direction moveDir;
    public PlayerProjectile(
            EngineCore engine,
            AffineTransform transform,
            Direction direction) {
        super(engine, transform);
        this.transform.scale(.5, .5);
        this.moveDir = direction;
        this.addGraphicsComponent(new Teardrop(this));
    }

    @Override
    public void move() {
        double vel = speed * 100 * EngineCore.clock.getDeltaTime();
        this.transform.translate(
                this.moveDir.delta.getX() * vel,
                this.moveDir.delta.getY() * vel
        );
    }

    @Override
    public void logic() {
        super.logic();
        this.move();
    }
}
