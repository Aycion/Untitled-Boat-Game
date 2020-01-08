package engine.math;

import engine.colliders.Collider;

import java.awt.geom.*;
import java.util.ArrayList;

public class Collisions {

    /**
     * Test and return whether the two provided {@link Collider}s are colliding.
     * <p>
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
        return testOverlappingBounds(a.getArea(), b.getArea())
                && testSATCollision(a.getArea(), b.getArea());
    }

    /**
     * Test the broad phase of the collision detection.
     * <p>
     * Takes the two areas representing each {@link Collider}, and
     * checks if their bounding boxes overlap. This does not necessarily
     * mean a collision has occurred, only that one is possible.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean testOverlappingBounds(Area a, Area b) {
        Rectangle2D boundA, boundB;

        // Get the bounding boxes of the two Areas
        boundA = a.getBounds2D();
        boundB = b.getBounds2D();

        // Test for bounding box overlap; true == overlap
        // (Note that y coords are top-down)
        return boundA.getMinX() < boundB.getMaxX()  // A's left to the left of B's right?
                && boundA.getMaxX() > boundB.getMinX()      // A's right to the right of B's left?
                && boundA.getMinY() < boundB.getMaxY()      // A's top above B's bottom?
                && boundA.getMaxY() > boundA.getMinY();
    }

    /**
     * Test whether the two possibly rotated {@link Area}s collide by way
     * of the Separating Axis Theorem.
     * <p>
     * First collect the vertices that make up each shape. Using those,
     * compile a list of unique normals, which are the unique vectors normal
     * to each side on both polygons. Project every vertex from both onto the
     * normals and identify the min and max points from each polygon. For each
     * projection, test for overlap between the minimum of the higher-positioned
     * polygon and the maximum of the lower-positioned polygon. If there is
     * overlap between the projections for each normal, a collision has occurred.
     * If a separation exists on any axis, then the polygons have not collided.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean testSATCollision(Area a, Area b) {
        ArrayList<Point2D> verticesA, verticesB, normals;

        // Get the path iterators from the areas.
        verticesA = getAreaVertices(a.getPathIterator(null));
        verticesB = getAreaVertices(b.getPathIterator(null));
        // Normals is a list of Vectors
        normals = getNormals(verticesA);
        normals.addAll(getNormals(verticesB));

        // We only need to store the limits of the projection
        double[] projA = new double[2],
                projB = new double[2];

        for (Point2D norm : normals) {
            getProjection(verticesA, norm, projA);
            getProjection(verticesB, norm, projB);

            // Immediately return false if there's no overlap
            if (projA[1] < projB[0] || projB[1] < projA[0]) {
                return false;
            }
        }

        // Return true if there's no separation, since there is a collision
        return true;
    }

    private static ArrayList<Point2D> getAreaVertices(PathIterator path) {
        double[] coords = new double[6];
        ArrayList<Point2D> coordArray = new ArrayList<>();
        for (; !path.isDone(); path.next()) {

            // Place the current vertex in the first two indices of coords
            path.currentSegment(coords);
            coordArray.add(new Point2D.Double(coords[0], coords[1]));
        }

        // Add the first point to the end for ease of processing
        coordArray.add(coordArray.get(0));
        return coordArray;
    }

    private static ArrayList<Point2D> getNormals(ArrayList<Point2D> verts) {
        ArrayList<Point2D> normals = new ArrayList<>();

        // Note: the last element of verts == the first; this will capture every edge
        for (int i = 0; i < verts.size() - 1; i++) {
            Point2D a, b, norm;

            a = verts.get(i);
            b = verts.get(i + 1);

            // Create a vector (represented just by a point) normal to the
            //  edge between i and i+1
            norm = new Point2D.Double(-(b.getY() - a.getY()), b.getX() - a.getX());
            normals.add(norm);
        }
        return normals;
    }

    private static void getProjection(ArrayList<Point2D> points, Point2D axis, double[] storeOut) {
        storeOut[0] = Double.MAX_VALUE;
        storeOut[1] = Double.MIN_VALUE;
        for (Point2D p : points) {
            double projection = dotProduct(p, axis);
            storeOut[0] = Math.min(projection, storeOut[0]);
            storeOut[1] = Math.max(projection, storeOut[1]);


        }
    }

    private static double dotProduct(Point2D point, Point2D axis) {
        double px = point.getX(), py = point.getY();
        double ax = axis.getX(), ay = axis.getY();

        return (px * ax) + (py * ay);
    }

}
