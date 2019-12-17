package game.environment;

import engine.EngineCore;
import engine.ResourceNotFound;

import java.awt.image.BufferedImage;

public class WaterTexture {
    BufferedImage waterImage;

    int tileSize;

    public WaterTexture() throws ResourceNotFound {
        this.waterImage = (BufferedImage) EngineCore.assets.getImage("waterlinesx256.png", 0);
        this.tileSize = this.waterImage.getWidth();
    }

    public int getTileSize() {
        return this.tileSize;
    }

    public BufferedImage getImage() {
        return this.waterImage;
    }
}
