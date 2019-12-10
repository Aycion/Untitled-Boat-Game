package game.environment;

import engine.EngineCore;
import engine.GameObject;
import engine.ResourceNotFound;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GameWorld extends GameObject {

    private WaterTexture water;
    private BufferedImage mapView;

    Dimension worldDimension;

    public GameWorld(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.worldDimension = new Dimension(
                engine.getWidth() * 10,
                engine.getHeight() * 10
        );

        this.initiative = -1;
        this.water = new WaterTexture();

    }

    @Override
    public void logic() {
        super.logic();

        this.mapView = new BufferedImage(
                engine.gameCamera.getViewWidth() + (2 * this.water.getSize()),
                engine.gameCamera.getViewHeight() + (2 * this.water.getSize()),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = this.mapView.createGraphics();

        // Start x and y at tile boundaries
        int startX = (engine.gameCamera.getViewOriginX() / this.water.getSize()) * this.water.getSize();
        int startY = (engine.gameCamera.getViewOriginY() / this.water.getSize()) * this.water.getSize();

        for (int x = startX - this.water.getSize();
             x <= engine.gameCamera.getViewWidth() + this.water.getSize();
             x += this.water.getSize()) {


            for (int y = startY - this.water.getSize();
                 y <= engine.gameCamera.getViewHeight() + this.water.getSize();
                 y += this.water.getSize()) {

                g.drawImage(this.water.getImage(), x, y, null);
            }
        }
    }

    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);

        g.drawImage(this.mapView, -engine.gameCamera.getViewOriginX(), -engine.gameCamera.getViewOriginY(), null);
    }
}
