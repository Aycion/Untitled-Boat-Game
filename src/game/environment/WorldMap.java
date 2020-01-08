package game.environment;

import engine.EngineCore;
import engine.GameObject;
import engine.ResourceNotFound;

import java.awt.geom.AffineTransform;

public class WorldMap extends GameObject {

    public WorldMap(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.initiative = -1;

        /*
         Add the draw area to the components, which will maintain and
         render the background image
         */
        AnimatedBackground animatedBackground = new AnimatedBackground(this, new WaterAnimation());
        this.addLogicComponent(animatedBackground);
        this.addGraphicsComponent(animatedBackground);
    }
}
