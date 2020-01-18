package engine.ecs;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * A component is a type of object that can change
 * the state of a GameObject
 *
 */
public abstract class GameComponent implements Comparable<GameComponent> {

    protected GameObject parent;

    public boolean active;
    public int priority;
    protected AffineTransform localTransform;

    public GameComponent(GameObject object) {
        this.parent = object;
        this.active = true;
        this.priority = 0;

        this.localTransform = new AffineTransform();
    }

    public GameComponent(GameObject object, int priority) {
        this(object);
        this.priority = priority;
    }

    public abstract AffineTransform getLocalTransform();

    public abstract AffineTransform getGlobalTransform();


    public GameObject getParent() {
        return this.parent;
    }

    @Override
    public int compareTo(GameComponent o) {
        return this.priority - o.priority;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(this.getClass());
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    public interface Updatable {
        void logic();
    }

    public interface Drawable {
        void graphic(Graphics2D g);

    }
}