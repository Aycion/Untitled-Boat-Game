package engine.graphics;

import engine.*;

import java.awt.geom.AffineTransform;

public class Camera extends GameObject implements Moveable {

    final int viewWidth, viewHeight;
    final int MIN_TRANSLATE_X, MIN_TRANSLATE_Y,
            MAX_TRANSLATE_X, MAX_TRANSLATE_Y;

    final double scaleMin, scaleMax, baseScaleRate, baseRotationRate;
    double scale, scaleRate, rotation, rotationRate;


    public Camera(EngineCore engine, AffineTransform transform) {
        super(engine, transform);

        this.viewWidth = engine.frame.getWidth();
        this.viewHeight = engine.frame.getHeight();

        this.MIN_TRANSLATE_X = EngineCore.gameWorld.L_BOUND_X;
        this.MIN_TRANSLATE_Y = EngineCore.gameWorld.L_BOUND_Y;
        this.MAX_TRANSLATE_X = EngineCore.gameWorld.U_BOUND_X - this.viewWidth;
        this.MAX_TRANSLATE_Y = EngineCore.gameWorld.U_BOUND_Y - this.viewHeight;

        this.scale = 1;
        this.scaleMin = 0.5;
        this.scaleMax = 1.2;
        this.baseScaleRate = 0.1;

        this.rotation = 0;
        this.baseRotationRate = 1;

    }

    public int getViewOriginX() {
        return (int) this.transform.getTranslateX();
    }

    public int getViewOriginY() {
        return (int) this.transform.getTranslateY();
    }


    public int getViewportWidth() {
        return this.viewWidth;
    }

    public int getViewportHeight() {
        return this.viewHeight;
    }

    private void zoomIn() {
        this.scale = Math.max(this.scaleMin, this.scale - this.scaleRate);
    }

    private void zoomOut() {
        this.scale = Math.min(this.scaleMax, this.scale + this.scaleRate);
    }

    private void rotLeft() {
        this.rotation = (this.rotation + this.rotationRate) % 360;
//        this.deltaTransform.rotate(this.rotationRate,
//                0.5 * this.viewWidth,
//                0.5 * this.viewHeight
//        );
    }

    private void rotRight() {
        this.rotation = (this.rotation - this.rotationRate) % 360;
//        this.deltaTransform.rotate(-this.rotationRate,
//                0.5 * this.viewWidth,
//                0.5 * this.viewHeight
//        );
    }

    private void readInput() {
        if (InputCaptor.bindingActive("UP")) {
            this.zoomIn();
        } else if (InputCaptor.bindingActive("DOWN")) {
            this.zoomOut();
        }

        if (InputCaptor.bindingActive("LEFT")) {
            this.rotLeft();
        } else if (InputCaptor.bindingActive("RIGHT")) {
            this.rotRight();
        }
    }


    @Override
    public void logic() {
        super.logic();

        // Update the movement constants
        this.scaleRate = this.baseScaleRate
                * EngineCore.clock.getDeltaTime()
                * GameObject.movementMult;

        this.rotationRate = this.baseRotationRate
                * EngineCore.clock.getDeltaTime()
                * GameObject.movementMult;

        this.readInput();
        this.move();

    }

    @Override
    public void move() {
        double tlX, tlY;

        /*
        Start of block for following player
         */
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

        // Scaling code
        AffineTransform scaleRotT = AffineTransform.getTranslateInstance(
                (this.getViewportWidth() / 2.0) - (this.scale * (this.getViewportWidth()) / 2.0),
                (this.getViewportHeight() / 2.0) - (this.scale * (this.getViewportHeight()) / 2.0)
        );
        scaleRotT.scale(this.scale, this.scale);

        // Rotation code - DOES NOT WORK
//        scaleRotT.rotate(this.rotation, this.viewWidth/2.0, this.viewHeight/2.0);

        // Apply scaling/rotation
        this.transform.concatenate(scaleRotT);

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
