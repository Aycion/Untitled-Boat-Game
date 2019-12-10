package engine.colliders;

import engine.GameObject;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class CircleCollider extends Collider {
    protected Ellipse2D.Double circle;

    public CircleCollider(GameObject parent, int priority, double circleRadius) {
        super(parent, priority);
        circle = new Ellipse2D.Double(0.0, 0.0, circleRadius, circleRadius);
        area = new Area(circle);
    }
}
