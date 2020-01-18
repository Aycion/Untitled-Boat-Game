package engine.colliders;

import engine.ecs.GameComponent;
import engine.GameAudio;
import engine.ecs.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;

import engine.math.Collisions;

import static engine.EngineCore.audio;

public abstract class Collider extends GameComponent implements GameComponent.Drawable, GameComponent.Updatable {

    public static ArrayList<Collidable> collidables = new ArrayList<>();

    protected Area area;
    protected Shape shape;
    Color color;

    public Collider(Collidable parent, int priority) {
        super((GameObject) parent, priority);

        this.color = Color.GREEN;
        collidables.add(parent);
    }

    protected void setArea(Shape s) {
        this.shape = s;
        this.area = new Area(s);
        this.area.transform(this.getGlobalTransform());
    }

    public Area getArea() {
        return this.area;
    }

    public boolean isColliding(Collider other) {
        return Collisions.isColliding(this, other);
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

        this.color = Color.GREEN;
        for (Collidable c : collidables) {
            if (c != this.parent) {
                Collider other = c.getCollider();

                if (this.isColliding(other)) {

                    audio.playSoundClip(GameAudio.CRASH_SOUND_FILENAME, 800);
                    this.color = Color.RED;
                    other.color = Color.RED;

                }
            }
        }
    }


    @Override
    public void graphic(Graphics2D g) {

        g.setStroke(new BasicStroke(2));
        g.setPaint(this.color);

        g.draw(this.getArea());

    }
}
