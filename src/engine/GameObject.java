package engine;
/*
Emery Bacon         emery390@gmail.com
Jeremy Tuthill      jtuthill@terpmail.umd.edu
*/



import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.PriorityQueue;

public class GameObject implements Comparable<GameObject>{

    public static final int movementMult = 10;

    private PriorityQueue<Component> logicComponents = new PriorityQueue<>();

    private PriorityQueue<Component> graphicsComponents = new PriorityQueue<>();
    protected AffineTransform transform;
    // Represents the change in transform of this object between
    //  the current and previous logic updates
    protected AffineTransform deltaTransform;


    public int initiative;  // The object's priority

    public EngineCore engine;

    /**
     * Initialize this GameObject with a coordinate at row, col
     *
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

        for (Component c : this.logicComponents) {
            c.logic();
        }

        // deltaXForm resets to the identity matrix
        this.deltaTransform.setToIdentity();
    }

    public void graphic(Graphics2D g) {
        for (Component c : this.graphicsComponents) {
            c.graphic(g);
        }
    }


    /**
     * Attach a new component to the queue.
     * <p>
     * Returns true if the component isn't already in the queue,
     * otherwise return false;
     *
     * @param c The new component to attach
     * @return True or false, depending on whether the {@link Component}
     * was successfully added to the collection
     */
    public boolean addLogicComponent(Component c) {

        if (!this.logicComponents.contains(c)) {
            this.logicComponents.add(c);

            return true;
        }
        return false;
    }

    /**
     * Get the requested logic component from the collection of
     * graphics {@link Component}s. Iterates through the Queue checking
     * for equality and returning that {@link Component} if it's found.
     * If no matching {@link Component} is found, return null.
     *
     * @param c An instance of the requested component
     * @return The requested component or null.
     */
    public Component getLogicComponent(Component c) {
        for (Component comp : this.logicComponents) {
            if (comp.getClass().equals(c.getClass())) {
                return comp;
            }
        }

        return null;
    }

    public boolean removeLogicComponent(Component c) {
        for (Component comp : this.logicComponents) {
            if (comp.getClass().equals(c.getClass())) {
                this.logicComponents.remove(comp);
                return true;
            }
        }

        return false;
    }

    /**
     * Add the specified graphics {@link Component} to the
     *
     * @param c The new {@link Component} to be added to the collection.
     * @return True or false, depending on whether the {@link Component}
     * was successfully added to the collection
     */
    public boolean addGraphicsComponent(Component c) {

        if (!this.graphicsComponents.contains(c)) {
            // If logicComponents doesn't already contain c, add c to
            //  the priority queue
            this.graphicsComponents.add(c);
            return true;
        }
        return false;
    }

    /**
     * Get the requested graphics component from the collection of
     * graphics {@link Component}s. Iterates through the Queue checking
     * for equality and returning that {@link Component} if it's found.
     * If no matching {@link Component} is found, return null.
     *
     * @param c An instance of the requested component
     * @return The requested {@link Component} or null.
     */
    public Component getGraphicsComponent(Component c) {
        for (Component comp : this.graphicsComponents) {
            if (comp.getClass().equals(c.getClass())) {
                return comp;
            }
        }

        return null;
    }

    public boolean removeGraphicsComponent(Component c) {
        for (Component comp : this.graphicsComponents) {
            if (comp.getClass().equals(c.getClass())) {
                this.graphicsComponents.remove(comp);
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        for (Component c : this.logicComponents) {
            hashCode = (int)Math.log(hashCode * c.hashCode());
        }
        return hashCode;
    }

    @Override
    public int compareTo(GameObject other) {
        return this.initiative - other.initiative;
    }
}