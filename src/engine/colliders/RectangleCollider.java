package engine.colliders;

import engine.GameObject;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class RectangleCollider extends Collider {
    protected Rectangle2D.Double rectangle;

    public RectangleCollider(GameObject parent, int priority, double rectangleWidth, double rectangleHeight) {
        super(parent, priority);
        rectangle = new Rectangle2D.Double(0.0, 0.0, rectangleWidth, rectangleHeight);
        area = new Area(rectangle);
    }
}
