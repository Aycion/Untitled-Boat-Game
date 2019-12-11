package engine;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * A component is a type of object that can change
 * the state of a GameObject
 *
 */
public abstract class Component implements Comparable<Component> {

    protected GameObject parent;

    public boolean active;
    public int priority;
    protected AffineTransform localTransform;

    public Component(GameObject object) {
        parent = object;
        this.localTransform = new AffineTransform();
        this.active = true;
        this.priority = 0;
    }

    public Component(GameObject object, int priority) {
        this(object);
        this.priority = priority;
    }

    public abstract AffineTransform getLocalTransform();

    public abstract AffineTransform getGlobalTransform();

    public void logic() {

    }

    public void graphic(Graphics2D g) {

    }


    @Override
    public int compareTo(Component o) {
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
}