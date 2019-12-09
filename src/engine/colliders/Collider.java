package engine.colliders;

import engine.Component;
import engine.GameObject;

import java.awt.geom.AffineTransform;

public abstract class Collider extends Component {

    public Collider(GameObject parent, int priority) {
        super(parent, priority);
    }

    public boolean isColliding(RectangleCollider other) {
        return false;
    }

    public boolean isColliding(CircleCollider other) {
        return false;
    }

    @Override
    public AffineTransform getGlobalTransform() {
        return parent.getTransform();
    }

    @Override
    public AffineTransform getLocalTransform() {
        return parent.getTransform();
    }
}
