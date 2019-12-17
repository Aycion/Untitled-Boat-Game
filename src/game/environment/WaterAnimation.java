package game.environment;

import engine.EngineCore;
import engine.ResourceNotFound;
import engine.graphics.Animatable;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class WaterAnimation extends WaterTexture {

    ArrayList<BufferedImage> waterImages;



    public WaterAnimation() throws ResourceNotFound {
        this.waterImages = EngineCore.assets.getImageList("animwater256.png");

        // Alias the first image in the list to set the current image
        this.waterImage = this.waterImages.get(0);
        this.tileSize = this.waterImages.get(0).getWidth();


    }

    public BufferedImage getFrameAt(int index) throws ArrayIndexOutOfBoundsException {
        return this.waterImages.get(index);
    }

    public ArrayList<BufferedImage> getAllFrames() {
        return this.waterImages;
    }

    public int getNumFrames() {
        return this.waterImages.size();
    }

    public BufferedImage getImageAt(int index) {
        return this.waterImages.get(index);
    }

}
