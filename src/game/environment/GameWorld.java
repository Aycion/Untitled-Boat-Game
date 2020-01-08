package game.environment;

import engine.EngineCore;
import engine.GameObject;

import java.awt.geom.AffineTransform;

public class GameWorld extends GameObject {

    public final int L_BOUND_X, L_BOUND_Y, U_BOUND_X, U_BOUND_Y;


    public GameWorld(EngineCore engine, AffineTransform transform) {
        super(engine, transform);

        this.L_BOUND_X = this.L_BOUND_Y = 0;
        this.U_BOUND_X = this.U_BOUND_Y = this.engine.frame.getWidth() * 5;

        this.initiative = 0;

    }

}
