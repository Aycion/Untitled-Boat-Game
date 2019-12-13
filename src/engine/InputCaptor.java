package engine;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.util.HashMap;


public class InputCaptor extends JComponent {

    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    public static HashMap<Integer, Boolean> activeKeyMap;
    private InputMap inputMap;
    private ActionMap actionMap;


    public InputCaptor() {
        this.inputMap = this.getInputMap(IFW);
        this.actionMap = this.getActionMap();
        activeKeyMap = new HashMap<>();
    }

    public void addActionKey(String key) {
        String keyName = key.toUpperCase().strip();     // Stupid-proof

        KeyStroke keyDown = KeyStroke.getKeyStroke(keyName);
        KeyStroke keyUp = KeyStroke.getKeyStroke("released " + keyName);

        if (activeKeyMap.containsKey(keyDown.getKeyCode())) {
            throw new IllegalArgumentException("Key already mapped!");
        }

        activeKeyMap.put(keyDown.getKeyCode(), false);

        String pressEvent = keyName + " down", releaseEvent = keyName + " up";

        this.inputMap.put(keyDown, pressEvent);
        this.actionMap.put(pressEvent, new PressAction(keyName));

        this.inputMap.put(keyUp, releaseEvent);
        this.actionMap.put(releaseEvent, new ReleaseAction(keyName));
    }

    public void addKeyboardMoveBinding() {
        /*
        Mappings for key pressed events
         */
        addActionKey("W");
        addActionKey("S");
        addActionKey("A");
        addActionKey("D");
        addActionKey("I");
        addActionKey("O");
        addActionKey("F");
        addActionKey("SPACE");

    }

    public static boolean bindingActive(int key) {
        return activeKeyMap.get(key);
    }

    public static boolean bindingActive(String key) {
        return bindingActive(KeyStroke.getKeyStroke(key).getKeyCode());
    }

    public void reset() {
        for (int i : activeKeyMap.keySet()) {
            activeKeyMap.replace(i, false);
        }
    }

    private static class PressAction extends AbstractAction {

        private KeyStroke actionKey;

        private PressAction(String key) {
            this.actionKey = KeyStroke.getKeyStroke(key);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            activeKeyMap.replace(actionKey.getKeyCode(), true);
        }
    }

    private static class ReleaseAction extends AbstractAction {

        private KeyStroke actionKey;

        private ReleaseAction(String key) {
            this.actionKey = KeyStroke.getKeyStroke(key);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            activeKeyMap.replace(this.actionKey.getKeyCode(), false);
        }
    }

}
