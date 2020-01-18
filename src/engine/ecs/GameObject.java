package engine.ecs;
/*
Emery Bacon         emery390@gmail.com
Jeremy Tuthill      jtuthill@terpmail.umd.edu
*/


import engine.EngineCore;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.PriorityQueue;

public class GameObject implements Comparable<GameObject> {

    public static final int movementMult = 10;

    private PriorityQueue<GameComponent.Updatable> logicComponents = new PriorityQueue<>();

    private PriorityQueue<GameComponent.Drawable> graphicsComponents = new PriorityQueue<>();
    protected AffineTransform transform;
    // Represents the change in transform of this object between
    //  the current and previous logic updates
    protected AffineTransform deltaTransform;


    public int initiative;  // The object's priority

    public EngineCore engine;

    /**
     * Initialize this GameObject with a coordinate at row, col
     */
    public GameObject(EngineCore engine, AffineTransform transform) {
        this.engine = engine;
        this.transform = transform;
        this.deltaTransform = new AffineTransform();

    }

    /**
     * Initialize the GameObject at 0, 0
     */
    public GameObject(EngineCore engine) {
        this(engine, new AffineTransform());
    }

    public AffineTransform getTransform() {
        return this.transform;
    }

    public AffineTransform getDeltaTransform() {
        return this.deltaTransform;
    }

    /**
     * Update the GameObject using the components
     * that make up {@code this.components}.
     */
    public void logic() {
        // Transform the object by the delta from
        //  the previous frame
        this.transform.concatenate(this.deltaTransform);

        for (GameComponent.Updatable u : this.logicComponents) {
            u.logic();
        }

        // deltaXForm resets to the identity matrix
        this.deltaTransform.setToIdentity();
    }

    public void graphic(Graphics2D g) {
        for (GameComponent.Drawable d : this.graphicsComponents) {
            d.graphic(g);
        }
    }

    public boolean attachComponent(GameComponent c) {
        boolean updatabe = this.addLogicComponent(c);
        boolean drawable = this.addGraphicsComponent(c);
        return updatabe || drawable;
    }

    public boolean detachComponent(GameComponent c) {
        boolean updatabe = this.removeLogicComponent(c);
        boolean drawable = this.removeGraphicsComponent(c);
        return updatabe || drawable;
    }

    public GameComponent getCompoment(GameComponent c) {
        return this.getLogicComponent(c) != null ?
                this.getGraphicsComponent(c) : null;
    }


    /**
     * Attach a new component to the queue.
     * <p>
     * Returns true if the component isn't already in the queue,
     * otherwise return false;
     *
     * @param c The new component to attach
     * @return True or false, depending on whether the {@link GameComponent}
     * was successfully added to the collection
     */
    private boolean addLogicComponent(GameComponent c) {
        if (c instanceof GameComponent.Updatable && !this.logicComponents.contains(c)) {
            return this.logicComponents.add((GameComponent.Updatable) c);
        }
        return false;
    }

    /**
     * Get the requested logic component from the collection of
     * graphics {@link GameComponent}s. Iterates through the Queue checking
     * for equality and returning that {@link GameComponent} if it's found.
     * If no matching {@link GameComponent} is found, return null.
     *
     * @param c An instance of the requested component
     * @return The requested component or null.
     */
    private GameComponent getLogicComponent(GameComponent c) {
        if (c instanceof GameComponent.Updatable) {
            for (GameComponent.Updatable comp : this.logicComponents) {
                if (comp.getClass().equals(c.getClass())) {
                    return (GameComponent) comp;
                }
            }
        }

        return null;
    }

    private boolean removeLogicComponent(GameComponent c) {
        if (c instanceof GameComponent.Updatable) {
            for (GameComponent.Updatable comp : this.logicComponents) {
                if (comp.getClass().equals(c.getClass())) {
                    return this.logicComponents.remove(comp);
                }
            }
        }

        return false;
    }

    /**
     * Add the specified graphics {@link GameComponent} to the
     *
     * @param c The new {@link GameComponent} to be added to the collection.
     * @return True or false, depending on whether the {@link GameComponent}
     * was successfully added to the collection
     */
    private boolean addGraphicsComponent(GameComponent c) {

        if (c instanceof GameComponent.Drawable && !this.graphicsComponents.contains(c)) {
            return this.graphicsComponents.add((GameComponent.Drawable) c);
        }
        return false;
    }

    /**
     * Get the requested graphics component from the collection of
     * graphics {@link GameComponent}s. Iterates through the Queue checking
     * for equality and returning that {@link GameComponent} if it's found.
     * If no matching {@link GameComponent} is found, return null.
     *
     * @param c An instance of the requested component
     * @return The requested {@link GameComponent} or null.
     */
    private GameComponent getGraphicsComponent(GameComponent c) {
        if (c instanceof GameComponent.Drawable) {
            for (GameComponent.Drawable comp : this.graphicsComponents) {
                if (comp.getClass().equals(c.getClass())) {
                    return (GameComponent) comp;
                }
            }
        }

        return null;
    }

    private boolean removeGraphicsComponent(GameComponent c) {
        if (c instanceof GameComponent.Drawable) {
            for (GameComponent.Drawable comp : this.graphicsComponents) {
                if (comp.getClass().equals(c.getClass())) {
                    return this.graphicsComponents.remove(comp);
                }
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        for (GameComponent.Updatable c : this.logicComponents) {
            hashCode = (int) Math.log(hashCode * c.hashCode());
        }
        for (GameComponent.Drawable c : this.graphicsComponents) {
            hashCode = (int) Math.log(hashCode * c.hashCode());
        }
        return hashCode;
    }

    @Override
    public int compareTo(GameObject other) {
        return this.initiative - other.initiative;
    }
}