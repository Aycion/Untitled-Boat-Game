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

    protected void setArea(Shape s) {
        this.area = new Area(s);
        this.area.transform(this.getGlobalTransform());
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
        this.area.transform(this.parent.getDeltaTransform());
    }

    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);
        g.setPaint(Color.BLACK);
        g.draw(area);
    }
}
