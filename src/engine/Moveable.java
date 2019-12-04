package engine;

import java.awt.*;
import java.awt.geom.Point2D;

public interface Moveable {
    void move();

    enum Direction {
        UP(0, -1),
        RIGHT(1, 0),
        DOWN(0, 1),
        LEFT(-1, 0);

        public Point2D delta;

        Direction(int dx, int dy) {
            this.delta = new Point(dx, dy);
        }
    }
}
