package game.environment;

import engine.Component;
import engine.EngineCore;
import engine.GameObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class DrawArea extends Component {

    BufferedImage mapView;
    WaterTexture texture;

    int areaSize, tileSize;

    public DrawArea(GameObject object, WaterTexture texture) {
        super(object);

        // Assign the texture the object will use to create the background
        this.texture = texture;
        this.tileSize = texture.getSize();

        // Get the width and height of the camera's viewport
        int viewPortWd = this.parent.engine.frame.getWidth();
        int viewPortHt = this.parent.engine.getHeight();

        // Calculate the size of the draw area
        // The camera rotates, so each side must
        //  be strictly larger than the viewport's diagonal
        this.areaSize = (int) Math.sqrt(
                (viewPortWd * viewPortWd) + (viewPortHt * viewPortHt)
        );
        // Tile-sized buffer area on each side
        this.areaSize += 2 * this.tileSize;

        this.mapView = new BufferedImage(
                this.areaSize, this.areaSize,
                BufferedImage.TYPE_INT_RGB);

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
     * Construct the image to be used as the background of the game.
     *
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
     *
     * NOTE: The graphics context passed as an argument is already transformed
     * relative to the camera
     * @param g
     */
    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);
        int camX = EngineCore.gameCamera.getViewOriginX();
        int camY = EngineCore.gameCamera.getViewOriginY();

        int drawOriginX = camX-(camX % this.tileSize);
        int drawOriginY = camY-(camY % this.tileSize);
        AffineTransform afX = AffineTransform.getTranslateInstance(
                drawOriginX, drawOriginY
        );
        g.drawImage(this.mapView, afX, null);
    }
}
