package engine;

import java.io.*;
import java.util.HashMap;
import javax.sound.sampled.*;

public class GameAudio {
    public static final String GAME_MUSIC_FILENAME = "Assets/Audio/music.wav";
    public static final String CANNON_SOUND_FILENAME = "Assets/Audio/cannon.wav";
    public static final String CRASH_SOUND_FILENAME = "Assets/Audio/crash.wav";

    private HashMap<String, long[]> intervals;

    public GameAudio() {
        loopSoundClip(GAME_MUSIC_FILENAME);
        intervals = new HashMap<>();
    }

    public void loopSoundClip(String filename) {
        try {
            AudioInputStream musicStream = AudioSystem.getAudioInputStream(new File(filename));
            Clip soundClip = AudioSystem.getClip();
            soundClip.open(musicStream);
            soundClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Encountered an error when looping a sound.");
        }
    }

    public void playSoundClip(String filename, int delayInMilliseconds) {
        long curTime = System.currentTimeMillis();

        if (!intervals.containsKey(filename)) {
            intervals.put(filename, new long[]{0, curTime});
        }

        long[] curInterval = intervals.get(filename);
        curInterval[1] = curTime;

        if (curInterval[1] - curInterval[0] >= delayInMilliseconds) {
            try {
                AudioInputStream cannonStream = AudioSystem.getAudioInputStream(new File(filename));
                Clip soundClip = AudioSystem.getClip();
                soundClip.open(cannonStream);
                soundClip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println("Encountered an error when playing a sound.");
            }
            curInterval[0] = curInterval[1];
        }
    }
}
