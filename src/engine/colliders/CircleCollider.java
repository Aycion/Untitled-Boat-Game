package engine.colliders;

import java.awt.geom.Ellipse2D;

public class CircleCollider extends Collider {

    public CircleCollider(Collidable parent, int priority, double circleRadius) {
        super(parent, priority);
        this.setArea(new Ellipse2D.Double(0.0, 0.0, circleRadius, circleRadius));
    }
}
