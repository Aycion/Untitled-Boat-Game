import engine.EngineCore;
import engine.GameAudio;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Driver {

    public static void main(String[] args) {
        EngineCore engine = new EngineCore(15,
                "Untitled Boat Game", "./Assets"
        );

        GameAudio audio = new GameAudio();
        engine.start();
    }
}
