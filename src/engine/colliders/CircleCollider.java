package engine.colliders;

import engine.GameObject;

import java.awt.geom.AffineTransform;

public class CircleCollider extends Collider {
    private double circleRadius;

    public CircleCollider(GameObject parent, int priority, double circleRadius) {
        super(parent, priority);
        this.circleRadius = circleRadius;
    }

    @Override
    public boolean isColliding(CircleCollider other) {
        AffineTransform transA = this.getGlobalTransform();
        AffineTransform transB = other.getGlobalTransform();
        double distanceX = transA.getTranslateX() - transB.getTranslateX();
        double distanceY = transA.getTranslateY() - transB.getTranslateY();
        double distance = Math.sqrt(Math.pow(distanceX, 2.0) + Math.pow(distanceY, 2.0));

        return distance <= this.circleRadius + other.circleRadius;
    }
}
