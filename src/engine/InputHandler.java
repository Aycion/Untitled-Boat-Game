package engine;

import java.awt.event.*;
import java.util.HashMap;

public class InputHandler implements KeyListener {

    public static HashMap<Integer, Boolean> actionMap;

    public InputHandler(EngineCore game) {
        actionMap = new HashMap<>();
    }

    public void reset() {
        for (int i : actionMap.keySet()) {
            actionMap.replace(i, false);
        }
    }

    public static void addActionKey(int key) {
        if (actionMap.containsKey(key)) {
            throw new IllegalArgumentException("Key already mapped!");
        }
        actionMap.put(key, false);
    }

    public static boolean bindingActive(int key) {
        return actionMap.get(key);
    }

    /*
    Key Listener methods
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (actionMap.containsKey(e.getKeyCode())) {
            actionMap.put(e.getKeyCode(), true);
        }

        /* Just for testing audio
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            EngineCore.audio.playCannonSound();
        }
        */
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (actionMap.containsKey(e.getKeyCode())) {
            actionMap.put(e.getKeyCode(), false);
        }
    }
}
