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
        this.mapView = new BufferedImage(
                engine.gameCamera.getViewWidth(),
                engine.gameCamera.getViewHeight(),
                BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void logic() {
        super.logic();
        Graphics2D g = this.mapView.createGraphics();

        // Start x and y at tile boundaries
        int startX = (engine.gameCamera.getViewOriginX()/water.getSize()) * water.getSize();
        int startY = (engine.gameCamera.getViewOriginY()/water.getSize()) * water.getSize();

        for (int x = startX - water.getSize();
             x <= engine.gameCamera.getViewWidth() + water.getSize();
             x += water.getSize()) {


            for (int y = startY - water.getSize();
                 y <= engine.gameCamera.getViewWidth() + water.getSize();
                 y += water.getSize()) {

                g.drawImage(water.getImage(), x, y, null);
            }
        }
    }

    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);

        g.drawImage(this.mapView, 0, 0, null);
    }
}
