package engine.colliders;

import java.awt.geom.Rectangle2D;

public class RectangleCollider extends Collider {

    public RectangleCollider(Collidable parent, int priority, double rectangleWidth, double rectangleHeight) {
        super(parent, priority);
        this.setArea(new Rectangle2D.Double(0.0, 0.0, rectangleWidth, rectangleHeight));
    }
}
