package game.environment;

import engine.ecs.GameComponent;
import engine.EngineCore;
import engine.ecs.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Background extends GameComponent implements GameComponent.Drawable, GameComponent.Updatable {

    // TODO - Split into two classes for Drawable and Updatable
    static final int DRAW_PADDING = 5;  // How many tiles to draw offscreen on each side
    BufferedImage mapImage;          // The image that will store the background
    WaterTexture texture;           // The tileable texture for the background

    int areaSize;                   // Pixel size of the overall background image
    int tileSize;                   // Pixel size of each tile
    int drawOriginX, drawOriginY, drawOffsetX, drawOffsetY;

    Color waterBase;

    /**
     *
     * @param parent the parent GameObject containing this object
     * @param texture the texture to paint as the background
     */
    public Background(GameObject parent, WaterTexture texture) {
        super(parent);


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
     * @param g the graphics object inherited from the GUI
     */
    @Override
    public void graphic(Graphics2D g) {

        AffineTransform afX = AffineTransform.getTranslateInstance(
                this.drawOriginX, this.drawOriginY
        );

        g.drawImage(this.mapImage, afX, null);
    }
}
