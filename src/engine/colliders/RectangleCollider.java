package engine.colliders;

import engine.GameObject;

import java.awt.*;
import java.awt.geom.Area;

public class RectangleCollider extends Collider {
    private Rectangle rectangle;
    private Area area;

    public RectangleCollider(GameObject parent, int priority, double rectangleHeight, double rectangleWidth) {
        super(parent, priority);
        rectangle = new Rectangle(0, 0, (int) rectangleWidth, (int) rectangleHeight);
        area = new Area(rectangle);
    }

    @Override
    public boolean isColliding(RectangleCollider other) {
        try {
            Area intersection = (Area) this.clone();
            intersection.intersect(other.area);
            return !intersection.isEmpty();
        } catch (CloneNotSupportedException e) {
            return false;
        }
    }

    @Override
    public void logic() {
        area.transform(parent.getTransform());
    }

    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);
        g.draw(area);
    }
}
