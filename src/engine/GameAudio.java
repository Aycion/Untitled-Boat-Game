package engine;

import java.io.*;
import javax.sound.sampled.*;

public class GameAudio {

    public GameAudio() {
        playGameMusic();
    }

    public void playGameMusic() {
        try {
            AudioInputStream musicStream = AudioSystem.getAudioInputStream(new File("Assets/Audio/music.wav"));
            Clip musicClip = AudioSystem.getClip();
            musicClip.open(musicStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error in audio initialization.");
        }
    }

    public void playCannonSound() {
        try {
            AudioInputStream cannonStream = AudioSystem.getAudioInputStream(new File("Assets/Audio/cannon.wav"));
            Clip cannonClip = AudioSystem.getClip();
            cannonClip.open(cannonStream);
            cannonClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Couldn't find cannon sound!");
        }
    }
}
