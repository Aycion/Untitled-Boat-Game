package game.environment;

import engine.EngineCore;
import engine.ecs.GameObject;
import engine.ResourceNotFound;

import java.awt.geom.AffineTransform;

public class WorldMap extends GameObject {

    public WorldMap(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.initiative = -10;

        /*
         Add the draw area to the components, which will maintain and
         render the background image
         */
        AnimatedBackground animatedBackground = new AnimatedBackground(this, new WaterAnimation());
        this.attachComponent(animatedBackground);
    }
}
