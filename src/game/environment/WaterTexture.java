package game.environment;

import engine.EngineCore;
import engine.ResourceNotFound;

import java.awt.image.BufferedImage;

public class WaterTexture {
    BufferedImage waterImage;

    int width, height;

    public WaterTexture() throws ResourceNotFound {
        this.waterImage = (BufferedImage) EngineCore.assets.getImage("water_big.png", 0);
        this.width = this.waterImage.getWidth();
        this.height = this.waterImage.getHeight();
    }

    public BufferedImage getImage() {
        return this.waterImage;
    }
}
