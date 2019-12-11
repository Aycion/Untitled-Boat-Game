package engine.colliders;

import engine.Component;
import engine.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public abstract class Collider extends Component {
    protected Area area;
    protected Shape shape;

    public Collider(GameObject parent, int priority) {
        super(parent, priority);
    }

    protected void setArea(Shape s) {
        this.shape = s;
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
    public AffineTransform getLocalTransform() {
        return this.localTransform;
    }

    @Override
    public AffineTransform getGlobalTransform() {
        AffineTransform t = new AffineTransform(this.parent.getDeltaTransform());
        t.concatenate(this.getLocalTransform());
        return t;
    }


    @Override
    public void logic() {
        this.area = new Area(this.shape);
        this.area.transform(this.getGlobalTransform());
    }

    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);
        g.setPaint(Color.BLACK);
        g.draw(area);
    }
}
