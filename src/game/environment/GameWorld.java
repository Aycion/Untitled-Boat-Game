package game.environment;

import engine.EngineCore;
import engine.GameObject;
import engine.ResourceNotFound;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GameWorld extends GameObject {

    private WaterTexture water;
    private BufferedImage worldMap;

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
    public void graphic(Graphics2D g) {
        super.graphic(g);
        g.drawImage(this.water.getImage(), 0, 0, null);
    }
}
