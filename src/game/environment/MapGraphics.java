package game.environment;

import engine.Component;
import engine.GameObject;

import java.awt.geom.AffineTransform;

public class MapGraphics extends Component {

    public MapGraphics(GameObject object) {
        super(object);
    }

    @Override
    public AffineTransform getLocalTransform() {
        return null;
    }

    @Override
    public AffineTransform getGlobalTransform() {
        return null;
    }
}
