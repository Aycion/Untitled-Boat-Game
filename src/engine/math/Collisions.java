package engine.math;

import engine.colliders.Collider;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Collisions {

    /**
     * Test and return whether the two provided {@link Collider}s are colliding.
     *
     * First check the broad phase of collision, which is bounding box overlap. If
     * there's no overlap, short-circuit the conditional and return false. If there
     * is, move on to the narrow phase. The narrow phase uses the Separating Axis
     * Theorem to check if the shapes themselves are overlapping. If they are, the
     * method will return true for a detected collision, otherwise false.
     *
     * @param a The first of the two {@link Collider}s to test
     * @param b The second of the two {@link Collider}s to test
     * @return True if a collision is detected, false otherwise
     */
    public static boolean isColliding(Collider a, Collider b) {
        // Short circuit if the bounding boxes don't overlap
        return hasBroadCollision(a.getArea(), b.getArea())
                && hasNarrowCollision(a.getArea(), b.getArea());
    }

    /**
     * Test the broad phase of the collision detection.
     *
     * Takes the two areas representing each {@link Collider}, and
     * checks if their bounding boxes overlap. This does not necessarily
     * mean a collision has occurred, only that one is possible.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean hasBroadCollision(Area a, Area b) {
        Rectangle2D boundA, boundB;
        double minXA, maxXA, minXB, maxXB,
                minYA, maxYA, minYB, maxYB;

        boundA = a.getBounds2D();
        boundB = b.getBounds2D();

        // Collect the bounds of the two areas
        minXA = boundA.getMinX();
        maxXA = boundA.getMaxX();
        minYA = boundA.getMinY();
        maxYA = boundA.getMaxY();

        minXB = boundB.getMinX();
        maxXB = boundB.getMaxX();
        minYB = boundB.getMinY();
        maxYB = boundB.getMaxY();

        // Test for bounding box overlap; true == overlap
        return minXA < maxXB        // A's left to the left of B's right?
                && maxXA > minXB    // A's right to the right of B's left?
                && minYA < maxYB    // A's top above B's bottom?
                && maxYA > minYB;   // A's bottom below B's top?
    }

    public static boolean hasNarrowCollision(Area a, Area b) {
        PathIterator pathA, pathB;

        pathA = a.getPathIterator(null);
        pathB = b.getPathIterator(null);
        return false;
    }
}
