package engine;
/*
Emery Bacon         emery390@gmail.com
Jeremy Tuthill      jtuthill@terpmail.umd.edu
*/



import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.PriorityQueue;

public class GameObject implements Comparable<GameObject>{

    public static int minObjectInit = 0;
    public static int maxObjectInit = 0;


    public static final int movementMult = 100;

    private PriorityQueue<Component> logicComponents = new PriorityQueue<>();

    private PriorityQueue<Component> graphicsComponents = new PriorityQueue<>();
    protected AffineTransform transform;


    public int initiative;  // The object's priority
    private boolean collidable;

    protected EngineCore engine;

    /**
     * Initialize this GameObject with a coordinate at row, col
     *
     */
    public GameObject(EngineCore engine, AffineTransform transform) {
        this.engine = engine;
        this.setCollidable(false);
        this.transform = transform;

    }

    /**
     * Initialize the GameObject at 0, 0
     */
    public GameObject(EngineCore engine) {
        this(engine, new AffineTransform());
    }

    public void setTransform(AffineTransform t) {
        if (this.transform.getTranslateX() < this.engine.getWidth())
        this.transform = t;
    }

    public AffineTransform getTransform() {
        return this.transform;
    }

    protected void setCollidable(boolean c) {
        this.collidable = c;
    }

    /**
     * Update the GameObject using the components
     * that make up {@code this.components}.
     */
    public void logic() {
        for (Component c : this.logicComponents) {
            c.logic();
        }
    }

    public void graphic(Graphics2D g) {
        for (Component c : this.graphicsComponents) {
            c.graphic(g);
        }
    }


    /**
     * Attach a new component to the queue thereof.
     * <p>
     * Returns true if the component isn't already in the queue,
     * otherwise return false;
     *
     * @param c The new component to attach
     * @return True or false, depending on whether the {@link Component}
     * was successfully added to the collection
     */
    public boolean addLogicComponent(Component c) {

        if (!logicComponents.contains(c)) {
            logicComponents.add(c);

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
        for (Component comp : logicComponents) {
            if (comp.equals(c)) {
                return comp;
            }
        }

        return null;
    }

    /**
     * Add the specified graphics {@link Component} to the
     *
     * @param c The new {@link Component} to be added to the collection.
     * @return True or false, depending on whether the {@link Component}
     * was successfully added to the collection
     */
    public boolean addGraphicsComponent(Component c) {

        if (!graphicsComponents.contains(c)) {
            // If logicComponents doesn't already contain c, add c to
            //  the priority queue
            graphicsComponents.add(c);
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
        for (Component comp : graphicsComponents) {
            if (comp.equals(c)) {
                return comp;
            }
        }

        return null;
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