package game;

import engine.EngineCore;
import engine.InputCaptor;
import engine.ResourceNotFound;

import java.awt.geom.AffineTransform;

public class Player extends Ship {

    public Player(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);
        EngineCore.inputCaptor.addKeyboardMoveBinding();

    }


    @Override
    public void move() {
        if (InputCaptor.bindingActive("W")) {
            this.accelerate();
        } else if (InputCaptor.bindingActive("S")) {
            this.decelerate();
        }

        if (InputCaptor.bindingActive("A")) {
            this.turnLeft();
        } else if (InputCaptor.bindingActive("D")) {
            this.turnRight();
        }

        super.move();
    }
}
