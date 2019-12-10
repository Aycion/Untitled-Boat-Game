package engine;

import java.io.*;
import javax.sound.sampled.*;

public class GameAudio {
    private Clip musicClip, cannonClip;
    private AudioInputStream musicStream, cannonStream;

    public GameAudio() {
        try {
            musicStream = AudioSystem.getAudioInputStream(new File("Assets/Audio/music.wav"));
            musicClip = AudioSystem.getClip();
            musicClip.open(musicStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);

            cannonStream = AudioSystem.getAudioInputStream(new File("Assets/Audio/cannon.wav"));
            cannonClip = AudioSystem.getClip();
            cannonClip.open(cannonStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error in audio initialization.");
        }
    }

    public void playCannonSound() {
        cannonClip.start();
    }
}
