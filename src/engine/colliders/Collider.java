package engine.colliders;

import engine.Component;
import engine.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public abstract class Collider extends Component {
    protected Area area;

    public Collider(GameObject parent, int priority) {
        super(parent, priority);
    }

    public boolean isColliding(Collider other) {
        try {
            Area intersection = (Area) this.clone();
            intersection.intersect(other.area);
            return !intersection.isEmpty();
        } catch (CloneNotSupportedException e) {
            return false;
        }
    }

    @Override
    public AffineTransform getGlobalTransform() {
        return parent.getTransform();
    }

    @Override
    public AffineTransform getLocalTransform() {
        return parent.getTransform();
    }

    @Override
    public void logic() {
        area.transform(new AffineTransform());
    }

    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);
        g.setPaint(Color.BLACK);
        g.draw(area);
    }
}
