package engine.math;

import java.awt.geom.Point2D;

public class Vector2D extends Point2D {

    double x;
    double y;

    public Vector2D(double x, double y) {
        super();
        this.setLocation(x, y);
    }

    /**
     * Return the dot product of the two provided 2D vectors.
     *
     * In two dimensions, the dot product is defined as
     * D = (A.x * B.x) + (A.y * B.y). The dot product represents
     * the projection of A onto B. That is to say, the dot product
     * is the magnitude of the vector A in the direction of B. If A
     * and B are orthogonal, their dot product is 0.
     *
     * @param a The first vector involved in the calculation
     * @param b The second vector involved in the calculation
     * @return The dot product of a and b
     */
    public static double dotProduct(Vector2D a, Vector2D b) {
        return (a.getX()*b.getX()) + (a.getY()*b.getY());
    }

    /**
     * Return the cross product of the two provided 2D vectors.
     *
     * In two dimensions, the cross product is defined as
     * C = (A.x * B.y) - (B.x * A.y). Since we're only working in
     * two dimensions, this product is a scalar that oscillates with
     * the angular separation between A and B. If A and B are parallel
     * or collinear, the cross product is 0.
     *
     * @param a The first vector involved in the calculation
     * @param b The second vector involved in the calculation
     * @return The scalar cross product between a and B
     */
    public static double crossProduct(Vector2D a, Vector2D b) {
        return (a.getX()*b.getY()) - (b.getX()*a.getY());
    }

    /**
     * Dot this {@link Vector2D} with the other provided
     * as an argument.
     *
     * @param other The {@link Vector2D} with which to dot this one
     * @return The dot product of this {@link Vector2D} and {@code other}
     */
    public double dotWith(Vector2D other) {
        return dotProduct(this, other);
    }

    /**
     * Cross this {@link Vector2D} with the other provided
     * as an argument.
     *
     * @param other The {@link Vector2D} with which to cross this one
     * @return The cross product of this {@link Vector2D} and {@code other}
     */
    public double crossWith(Vector2D other) {
        return crossProduct(this, other);
    }

    /**
     * Returns the X component of this {@link Vector2D} with double
     * precision
     *
     * @return The X component of this {@link Vector2D}
     */
    @Override
    public double getX() {
        return this.x;
    }

    /**
     * Returns the Y component of this {@link Vector2D} with double
     * precision
     *
     * @return The Y component of this {@link Vector2D}
     */
    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
