package engine.graphics;

import engine.EngineCore;
import engine.GameObject;
import engine.Moveable;

import java.awt.geom.AffineTransform;

public class Camera extends GameObject implements Moveable {

    final int viewWidth, viewHeight;

    public Camera(EngineCore engine, AffineTransform transform) {
        super(engine, transform);
        this.viewWidth = engine.frame.getWidth();
        this.viewHeight = engine.frame.getHeight();
    }

    public int getViewOriginX() {
        return (int) this.transform.getTranslateX();
    }

    public int getViewOriginY() {
        return (int) this.transform.getTranslateY();
    }


    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    @Override
    public void logic() {
        super.logic();
        this.move();
    }

    @Override
    public void move() {
        this.transform.translate(10, 10);

    }


    /**
     * Get the coordinates of the transform relative to the screen's coordinates
     *
     * @param t The transform on which to operate
     * @return
     */
    public AffineTransform worldToScreenSpace(AffineTransform t) {

        double objX = t.getTranslateX();
        double objY = t.getTranslateY();
        double camX = this.transform.getTranslateX();
        double camY = this.transform.getTranslateY();

        AffineTransform screenSpaceXForm = new AffineTransform();
        screenSpaceXForm.setToTranslation(objX - camX, objY - camY);

        return screenSpaceXForm;
    }

    /**
     * Check that the {@link GameObject} gObj is in the camera frame, with a buffer
     * of half the view size on each side.
     *
     * @param gObj
     * @return
     */
    public boolean isInCameraFrame(GameObject gObj) {

        double xLowBound = this.transform.getTranslateX() - (this.viewWidth / 2.0);
        double yLowBound = this.transform.getTranslateY() - (this.viewWidth / 2.0);
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
