package game.environment;

import engine.EngineCore;
import engine.ResourceNotFound;

import java.awt.image.BufferedImage;

public class WaterTexture {
    BufferedImage waterImage;

    int size;

    public WaterTexture() throws ResourceNotFound {
        this.waterImage = (BufferedImage) EngineCore.assets.getImage("water.png", 0);
        this.size = this.waterImage.getWidth();
    }

    public int getSize() {
        return size;
    }

    public BufferedImage getImage() {
        return this.waterImage;
    }
}
