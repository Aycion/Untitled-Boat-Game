package engine.graphics;

import engine.EngineCore;
import engine.GameObject;

import java.awt.geom.AffineTransform;

public class Camera {

    int viewWidth, viewHeight;
    AffineTransform transform;

    public Camera(EngineCore engine, AffineTransform transform) {
        this.viewWidth = engine.getWidth();
        this.viewHeight = engine.getHeight();
        this.transform = transform;
    }

    /**
     * Get the coordinates of the transform relative to the screen's coordinates
     * @param t The transform on which to operate
     * @return
     */
    public AffineTransform worldToScreenSpace(AffineTransform t) {
        AffineTransform gObj = (AffineTransform) t.clone();
        gObj.concatenate(this.transform);

        return gObj;
    }

    /**
     * Check that the {@link GameObject} gObj is in the camera frame, with a buffer
     * of half the view size on each side.
     *
     * @param gObj
     * @return
     */
    public boolean isInCameraFrame(GameObject gObj) {

        double xLowBound = this.transform.getTranslateX() - (this.viewWidth/2.0);
        double yLowBound = this.transform.getTranslateY() - (this.viewWidth/2.0);
        double xHighBound = xLowBound + (this.viewWidth * 1.5);
        double yHighBound = yLowBound + (this.viewHeight * 1.5);
        AffineTransform objTransform = gObj.getTransform();

        double objX, objY;
        objX = objTransform.getTranslateX();
        objY = objTransform.getTranslateY();

        return xLowBound < objX && objX < xHighBound &&
                yLowBound < objY && objY < yHighBound;

    }
}
