package game.environment;

import engine.EngineCore;
import engine.GameObject;
import engine.graphics.Animatable;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimatedBackground extends Background implements Animatable {

    private int imageIndex, animationFrames;
    private double animationDuration, animationPercent;

    ArrayList<BufferedImage> backgroundImages;
    /**
     * @param object
     * @param texture
     */
    public AnimatedBackground(GameObject object, WaterAnimation texture) {
        super(object, texture);

        this.backgroundImages = new ArrayList<>();
        this.animationFrames = texture.getNumFrames();
        this.imageIndex = 0;
        this.animationPercent = 0;
        this.animationDuration = 2;

        for (BufferedImage frame : texture.getAllFrames()) {
            backgroundImages.add(this.constructMapView(frame));
        }
    }

    @Override
    public BufferedImage getAnimationFrame() {
        if ((this.animationPercent += (EngineCore.clock.getDeltaTime() / this.animationDuration)
        ) * 100 > 100) {
            this.animationPercent = 0;
        }
        this.imageIndex = (int) (this.animationPercent * this.animationFrames);
        return backgroundImages.get(imageIndex);
    }

    @Override
    public BufferedImage getFrameAt(int index) throws ArrayIndexOutOfBoundsException {
        return this.backgroundImages.get(index);
    }

    @Override
    public ArrayList<BufferedImage> getAllFrames() {
        return this.backgroundImages;
    }

    @Override
    public void logic() {
        super.logic();
        this.mapImage = this.getAnimationFrame();
    }
}
