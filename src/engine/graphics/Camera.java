package engine.graphics;

import engine.EngineCore;
import engine.GameObject;
import engine.Moveable;

import java.awt.geom.AffineTransform;

public class Camera extends GameObject implements Moveable {

    final int viewWidth, viewHeight;
    final int MIN_TRANSLATE_X, MIN_TRANSLATE_Y,
            MAX_TRANSLATE_X, MAX_TRANSLATE_Y;

    public Camera(EngineCore engine, AffineTransform transform) {
        super(engine, transform);
        this.viewWidth = engine.frame.getWidth();
        this.viewHeight = engine.frame.getHeight();

        MIN_TRANSLATE_X = EngineCore.gameWorld.L_BOUND_X;
        MIN_TRANSLATE_Y = EngineCore.gameWorld.L_BOUND_Y;
        MAX_TRANSLATE_X = EngineCore.gameWorld.U_BOUND_X - this.viewWidth;
        MAX_TRANSLATE_Y = EngineCore.gameWorld.U_BOUND_Y - this.viewHeight;

    }

    public int getViewOriginX() {
        return (int) this.transform.getTranslateX();
    }

    public int getViewOriginY() {
        return (int) this.transform.getTranslateY();
    }


    public int getViewWidth() {
        return this.viewWidth;
    }

    public int getViewHeight() {
        return this.viewHeight;
    }

    @Override
    public void logic() {
        super.logic();
        this.move();
    }

    @Override
    public void move() {
        double tlX, tlY;
        AffineTransform anchorPoint = new AffineTransform(EngineCore.player.getTransform());
        anchorPoint.translate(EngineCore.player.getRotAnchorX(), EngineCore.player.getRotAnchorY());
        tlX = anchorPoint.getTranslateX() - (0.5 * this.viewWidth);
        tlY = anchorPoint.getTranslateY() - (0.5 * this.viewHeight);

        tlX = Math.max(
                this.MIN_TRANSLATE_X,
                Math.min(this.MAX_TRANSLATE_X, tlX)
        );

        tlY = Math.max(
                this.MIN_TRANSLATE_Y,
                Math.min(this.MAX_TRANSLATE_Y, tlY)
        );

        this.transform.setToTranslation(tlX, tlY);

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

    public AffineTransform screenToWorldSpace(AffineTransform t) {
        AffineTransform worldSpaceXForm = (AffineTransform) this.transform.clone();
        worldSpaceXForm.concatenate(t);

        return worldSpaceXForm;
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
