package engine;

import java.awt.*;
import java.awt.geom.Point2D;

public interface Moveable {
    void move();

    enum Direction {
        UP(0, -1, 0),
        LEFT(-1, 0, 1),
        DOWN(0, 1, 2),
        RIGHT(1, 0, 3);

        public Point2D delta;
        public int quadrant;

        Direction(int dx, int dy, int quadrant) {
            this.delta = new Point(dx, dy);
            this.quadrant = quadrant;
        }
    }
}
