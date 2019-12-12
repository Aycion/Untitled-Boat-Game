package engine;

import java.io.*;
import java.util.HashMap;
import javax.sound.sampled.*;

public class GameAudio {
    public static final String GAME_MUSIC_FILENAME = "Assets/Audio/music.wav";
    public static final String CANNON_SOUND_FILENAME = "Assets/Audio/cannon.wav";
    public static final String CRASH_SOUND_FILENAME = "Assets/Audio/crash.wav";

    private long prev_ts, cur_ts;
    private HashMap<String, long[]> intervals;

    public GameAudio() {
        playGameMusic();
        prev_ts = 0;
        cur_ts = 0;
        intervals = new HashMap<>();
    }

    public void playGameMusic() {
        try {
            AudioInputStream musicStream = AudioSystem.getAudioInputStream(new File(GAME_MUSIC_FILENAME));
            Clip musicClip = AudioSystem.getClip();
            musicClip.open(musicStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error in audio initialization.");
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
