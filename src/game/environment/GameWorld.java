package game.environment;

import engine.EngineCore;
import engine.GameClock;
import engine.GameObject;
import engine.ResourceNotFound;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GameWorld extends GameObject {

    private WaterTexture water;
    private BufferedImage mapView;
    private int tileSize;

    Dimension worldDimension;

    public GameWorld(EngineCore engine, AffineTransform transform) throws ResourceNotFound {
        super(engine, transform);

        this.worldDimension = new Dimension(
                engine.getWidth() * 10,
                engine.getHeight() * 10
        );

        this.initiative = -1;
        this.water = new WaterTexture();

        this.tileSize = this.water.getSize();

    }

    @Override
    public void logic() {
        super.logic();

        this.mapView = new BufferedImage(
                this.engine.gameCamera.getViewWidth() + (2 * this.tileSize),
                this.engine.gameCamera.getViewHeight() + (2 * this.tileSize),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = this.mapView.createGraphics();

        int camX, camY;
        camX = this.engine.gameCamera.getViewOriginX();
        camY = this.engine.gameCamera.getViewOriginY();

        // Start x and y at tile boundaries
        int startX = camX - (camX % this.tileSize);
        int startY = camY - (camY % this.tileSize);


        for (int x = startX;
             x <= startX + this.engine.gameCamera.getViewWidth() + (2*this.tileSize);
             x += this.tileSize) {


            for (int y = startY;
                 y <= startY + this.engine.gameCamera.getViewHeight() + (2*this.tileSize);
                 y += this.tileSize) {
                g.drawImage(this.water.getImage(), x - camX, y - camY, null);
            }
        }

        g.dispose();
    }


    @Override
    public void graphic(Graphics2D g) {
        super.graphic(g);
        AffineTransform afX = new AffineTransform();
        afX.setToTranslation(-this.tileSize, -this.tileSize);
        g.drawImage(this.mapView, afX, null);
    }

    public double sineDisplacement() {
        float x = EngineCore.clock.getLastTime() / GameClock.timeUnitsPerSecond;
        return (Math.sin(x) +
                Math.sin((2.2*x) + 5.52) +
                Math.sin((2.9*x) + 0.93) +
                Math.sin((4.6*x) + 8.94));
    }

}
