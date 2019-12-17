package game.environment;

import engine.Component;
import engine.EngineCore;
import engine.GameClock;
import engine.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Background extends Component {

    static final int DRAW_PADDING = 5;  // How many tiles to draw offscreen on each side
    BufferedImage mapImage;          // The image that will store the background
    WaterTexture texture;           // The tileable texture for the background

    int areaSize;                   // Pixel size of the overall background image
    int tileSize;                   // Pixel size of each tile
    int drawOriginX, drawOriginY, drawOffsetX, drawOffsetY;

    Color waterBase;

    /**
     *
     * @param object
     * @param texture
     */
    public Background(GameObject object, WaterTexture texture) {
        super(object);


        // Assign the texture the object will use to create the background
        this.texture = texture;
        this.tileSize = texture.getTileSize();

        this.setConstants();

        this.mapImage = this.constructMapView(this.mapImage);
    }


    protected void setConstants() {
        // Set the background color (underlay beneath the ripple texture)
        this.waterBase = Color.decode("#027dbd");

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

        this.drawOffsetX = (this.areaSize - viewPortWd) / 2;
        this.drawOffsetY = (this.areaSize - viewPortHt) / 2;
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
    protected BufferedImage constructMapView(BufferedImage image) {
        BufferedImage img = new BufferedImage(
                this.areaSize, this.areaSize,
                BufferedImage.TYPE_INT_ARGB_PRE
        );
        Graphics2D g = img.createGraphics();
        for (int x = 0; x <= img.getWidth(); x += this.tileSize) {
            for (int y = 0; y <= img.getHeight(); y += this.tileSize) {
                g.drawImage(image, x, y, null);
            }
        }

        g.dispose();
        return img;
    }

    @Override
    public void logic() {
        super.logic();
        int camX = EngineCore.gameCamera.getViewOriginX();
        int camY = EngineCore.gameCamera.getViewOriginY();

        this.drawOriginX = camX - this.drawOffsetX - (camX % this.tileSize);
        this.drawOriginY = camY - this.drawOffsetY - (camY % this.tileSize);
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

        AffineTransform afX = AffineTransform.getTranslateInstance(
                this.drawOriginX, this.drawOriginY
        );

        g.setColor(this.waterBase);
        g.fillRect(drawOriginX, drawOriginY, this.areaSize, this.areaSize);

        g.drawImage(this.mapImage, afX, null);
    }
}
