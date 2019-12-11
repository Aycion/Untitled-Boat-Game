package engine;

import java.awt.*;
import java.awt.geom.Point2D;

public interface Moveable {
    void move();

    enum Direction {
        LEFT(-1, 0, 0),
        DOWN(0, 1, 90),
        RIGHT(1, 0, 180),
        UP(0, -1, 270);

        public Point2D delta;
        public int rotAngle;

        Direction(int dx, int dy, int rotAngle) {
            this.delta = new Point(dx, dy);
            this.rotAngle = rotAngle;
        }

        public static double getAngleDiff(Direction a, Direction b) {
            return (a.rotAngle - b.rotAngle) % 360;
        }

        public double getAngleDiff(Direction other) {
            return getAngleDiff(this, other);
        }
    }
}
