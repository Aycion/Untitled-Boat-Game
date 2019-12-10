package game;

import engine.EngineCore;
import engine.InputCaptor;

import java.awt.geom.AffineTransform;

public class Player extends Ship {

    public Player(EngineCore engine, AffineTransform transform) {
        super(engine, transform);
        EngineCore.inputCaptor.addKeyboardMoveBinding();
    }


    @Override
    public void move() {
        int dx = 0, dy = 0;
        if (InputCaptor.bindingActive("W")) {
            dy = -1;
        } else if (InputCaptor.bindingActive("S")) {
            dy = 1;
        }

        if (InputCaptor.bindingActive("A")) {
            dx = -1;
        } else if (InputCaptor.bindingActive("D")) {
            dx = 1;
        }
        this.transform.translate(this.speed * dx, this.speed * dy);
    }
}
