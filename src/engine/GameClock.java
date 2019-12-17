package engine;

public class GameClock {
    private long startTime, lastTime, timeDelta;

    // Ratio for converting clock time to seconds
    public static final float timeUnitsPerSecond = 1000000000f;

    GameClock() {
        this.startTime = this.lastTime = System.nanoTime();

    }

    void update() {
        this.timeDelta = System.nanoTime() - this.lastTime;
        this.lastTime = System.nanoTime();

    }

    public float getLastTime() {
        return this.lastTime;
    }

    public float getDeltaTime() {
        return (float) this.timeDelta / timeUnitsPerSecond;
    }

    public float getTotalElapsedTime() {
        return (float) (System.nanoTime() - this.startTime) / timeUnitsPerSecond;
    }
}
