package game.environment;

import engine.EngineCore;
import engine.GameClock;
import engine.GameObject;
import engine.ResourceNotFound;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GameWorld extends GameObject {

    public final int L_BOUND_X, L_BOUND_Y, U_BOUND_X, U_BOUND_Y;

    Dimension worldDimension;

    public GameWorld(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        L_BOUND_X = L_BOUND_Y = 0;
        U_BOUND_X = U_BOUND_Y = this.engine.frame.getWidth() * 10;

        this.worldDimension = new Dimension(U_BOUND_X, U_BOUND_Y);

        this.initiative = -1;

        // Add the draw area to the components, which will maintain and
        //  render the background image
        this.addGraphicsComponent(new DrawArea(this, new WaterTexture()));

    }


    /**
     * "Animation" function for displacing pixels. Incomplete and currently slow.
     * @return
     */
    public double sineDisplacement() {
        float x = EngineCore.clock.getLastTime() / GameClock.timeUnitsPerSecond;
        return (Math.sin(x) +
                Math.sin((2.2*x) + 5.52) +
                Math.sin((2.9*x) + 0.93) +
                Math.sin((4.6*x) + 8.94));
    }

}
