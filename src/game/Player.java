package game;

import engine.EngineCore;
import engine.InputCaptor;
import engine.ResourceNotFound;

import java.awt.geom.AffineTransform;

public class Player extends Ship {

    public Player(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);
        EngineCore.inputCaptor.addKeyboardMoveBinding();
        this.direction = Direction.UP;

    }


    @Override
    public void move() {
        super.move();
        double velocity = this.speed * movementMult * EngineCore.clock.getDeltaTime();
        if (InputCaptor.bindingActive("W")) {
            this.changeDirection(Direction.UP);
        } else if (InputCaptor.bindingActive("S")) {
            this.changeDirection(Direction.DOWN);
        } else if (InputCaptor.bindingActive("A")) {
            this.changeDirection(Direction.LEFT);
        } else if (InputCaptor.bindingActive("D")) {
            this.changeDirection(Direction.RIGHT);
        } else {
            // Return and don't move if no keys are down
            return;
        }
        this.deltaTransform.translate(0, -velocity);
//        this.transform.concatenate(this.deltaTransform);
    }
}
