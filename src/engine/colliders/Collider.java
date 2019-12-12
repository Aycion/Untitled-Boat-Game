package engine.colliders;

import engine.Component;
import engine.EngineCore;
import engine.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public abstract class Collider extends Component {
    public static final RectangleCollider dummyRectangle = new RectangleCollider(null, 0, 0, 0);
    public static final CircleCollider dummyCircle = new CircleCollider(null, 0, 0);

    protected Area area;
    protected Shape shape;
    Color color;

    public Collider(GameObject parent, int priority) {
        super(parent, priority);
        color = Color.GREEN;
    }

    protected void setArea(Shape s) {
        this.shape = s;
        this.area = new Area(s);
        this.area.transform(this.getGlobalTransform());
    }

    public boolean isColliding(Collider other) {
        /*
        boolean retVal = false;
        this.setArea(shape);
        this.area.intersect(other.area);
        retVal = this.area.isEmpty();
        this.setArea(shape);
        return retVal;
        */
        Area temp = (Area) this.area.clone();
        temp.intersect(other.area);
        return temp.isEmpty();
    }

    @Override
    public AffineTransform getLocalTransform() {
        return this.localTransform;
    }

    @Override
    public AffineTransform getGlobalTransform() {
        AffineTransform t = new AffineTransform(super.parent.getTransform());
        t.concatenate(this.getLocalTransform());
        return t;
    }


    @Override
    public void logic() {
        this.area = new Area(this.shape);
        this.area.transform(this.getGlobalTransform());

        EngineCore core = super.parent.engine;
        this.color = Color.GREEN;
        for (GameObject object : core.elements) {
            RectangleCollider r = (RectangleCollider) object.getLogicComponent(dummyRectangle);
            CircleCollider c = (CircleCollider) object.getLogicComponent(dummyCircle);

            if (r != null && this.isColliding(r)) {
                // audio.playCannonSound();
                this.color = Color.RED;
                r.color = Color.RED;
            }

            if (c != null && this.isColliding(c)) {
                // audio.playCannonSound();
                this.color = Color.RED;
                c.color = Color.RED;
            }
        }
    }

    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);
        g.setPaint(color);
        g.draw(area);
    }
}
