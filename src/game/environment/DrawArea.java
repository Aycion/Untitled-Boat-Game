package game.environment;

import engine.Component;
import engine.EngineCore;
import engine.GameClock;
import engine.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class DrawArea extends Component {

    static final int DRAW_PADDING = 2;  // How many tiles to draw offscreen on each side

    BufferedImage mapView;          // The image that will store the background
    WaterTexture texture;           // The tileable texture for the background

    int areaSize;                   // Pixel size of the overall background image
    int tileSize;                   // Pixel size of each tile
    int drawOffsetX, drawOffsetY;

    public DrawArea(GameObject object, WaterTexture texture) {
        super(object);

        // Assign the texture the object will use to create the background
        this.texture = texture;
        this.tileSize = texture.getSize();

        // Get the width and height of the camera's viewport
        int viewPortWd = EngineCore.gameCamera.getViewportWidth();
        int viewPortHt = EngineCore.gameCamera.getViewportHeight();

        // Calculate the size of the draw area
        // The camera rotates, so each side must
        //  be strictly larger than the viewport's diagonal
        this.areaSize = (int) Math.sqrt(
                (viewPortWd * viewPortWd) + (viewPortHt * viewPortHt)
        );
        // Tile-sized buffer area on each side
        this.areaSize += 2 * DRAW_PADDING * this.tileSize;

        this.mapView = new BufferedImage(
                this.areaSize, this.areaSize,
                BufferedImage.TYPE_INT_RGB);

        this.drawOffsetX = (this.areaSize - viewPortWd) / 2;
        this.drawOffsetY = (this.areaSize - viewPortHt) / 2;

        this.constructMapView();
    }

    @Override
    public AffineTransform getLocalTransform() {
        return this.localTransform;
    }

    @Override
    public AffineTransform getGlobalTransform() {
        return null;
    }

    /**
     * "Animation" function for resampling pixels. Incomplete and currently slow af.
     *
     * Intended to recreate the water shader from Wind Waker
     * @return
     */
    public double sineDisplacement() {
        float x = EngineCore.clock.getLastTime() / GameClock.timeUnitsPerSecond;
        return (Math.sin(x) +
                Math.sin((2.2 * x) + 5.52) +
                Math.sin((2.9 * x) + 0.93) +
                Math.sin((4.6 * x) + 8.94));
    }

    /**
     * Construct the image to be used as the background of the game.
     * <p>
     * Caches the image once built so it can be reused indefinitely,
     * boosting performance since it doesn't need to redraw each tile
     * every update.
     */
    private void constructMapView() {
        Graphics2D g = this.mapView.createGraphics();
        for (int x = 0; x <= this.mapView.getWidth(); x += this.tileSize) {
            for (int y = 0; y <= this.mapView.getHeight(); y += this.tileSize) {
                g.drawImage(this.texture.getImage(), x, y, null);
            }
        }

        g.dispose();
    }

    /**
     * Draw the world onto the screen.
     * <p>
     * NOTE: The graphics context passed as an argument is already transformed
     * relative to the camera
     *
     * @param g
     */
    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);
        int camX = EngineCore.gameCamera.getViewOriginX();
        int camY = EngineCore.gameCamera.getViewOriginY();

        int drawOriginX = camX - this.drawOffsetX - (camX % this.tileSize);
        int drawOriginY = camY - this.drawOffsetY - (camY % this.tileSize);
        AffineTransform afX = AffineTransform.getTranslateInstance(
                drawOriginX, drawOriginY
        );
        g.drawImage(this.mapView, afX, null);
    }
}
