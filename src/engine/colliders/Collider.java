package engine.colliders;

import engine.Component;
import engine.EngineCore;
import engine.GameAudio;
import engine.GameObject;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import static engine.EngineCore.audio;

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
        Area temp = (Area) this.area.clone();
        temp.intersect(other.area);
        return !temp.isEmpty();
    }

    @Override
    public AffineTransform getLocalTransform() {
        return this.localTransform;
    }

    @Override
    public AffineTransform getGlobalTransform() {
        AffineTransform t = new AffineTransform(this.parent.getTransform());
        t.concatenate(this.getLocalTransform());
        return t;
    }


    @Override
    public void logic() {
        // Update the area with the parent's current transform
        this.area = new Area(this.shape);
        this.area.transform(this.getGlobalTransform());

        EngineCore core = this.parent.engine;
        this.color = Color.GREEN;
        for (GameObject object : core.elements) {
            if (object != this.parent) {
                RectangleCollider r = (RectangleCollider) object.getLogicComponent(dummyRectangle);
                CircleCollider c = (CircleCollider) object.getLogicComponent(dummyCircle);

                if (r != null && this.isColliding(r)) {
                    audio.playSoundClip(GameAudio.CRASH_SOUND_FILENAME, 800);
                    this.color = Color.RED;
                    r.color = Color.RED;
                }

                if (c != null && this.isColliding(c)) {
                    audio.playSoundClip(GameAudio.CRASH_SOUND_FILENAME, 800);
                    this.color = Color.RED;
                    c.color = Color.RED;
                }
            }
        }
    }

    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);
        g.setStroke(new BasicStroke(2));
        g.setPaint(this.color);
        g.draw(this.area);
    }
}
