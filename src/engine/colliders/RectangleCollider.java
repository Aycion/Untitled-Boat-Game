package engine.colliders;

import engine.GameObject;

public class RectangleCollider extends Collider {
    private double rectangleHeight, rectangleWidth;

    public RectangleCollider(GameObject parent, int priority, double rectangleHeight, double rectangleWidth) {
        super(parent, priority);
        this.rectangleHeight = rectangleHeight;
        this.rectangleWidth = rectangleWidth;
    }
}
