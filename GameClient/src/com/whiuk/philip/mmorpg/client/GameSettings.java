package com.whiuk.philip.mmorpg.client;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

/**
 * Game Settings.
 * @author Philip
 *
 */
public final class GameSettings {
    /**
     * Singleton.
     */
    private static final GameSettings SETTINGS = new GameSettings();
    /**
     * @return settings
     */
    public static GameSettings getSettings() {
        return SETTINGS;
    }
    /**
     * Controls.
     * @author Philip
     *
     */
    public static enum Control {
        /**
         * Move forwards.
         */
        MOVE_FOREWARD,
        /**
         * Move backwards.
         */
        MOVE_BACKWARD,
        /**
         * Turn left.
         */
        TURN_LEFT,
        /**
         * Turn right.
         */
        TURN_RIGHT
    }
    /**
     * Control mappings.
     */
    private Map<Control, Integer> controls;
    /**
     * 
     */
    private boolean fullscreen;
    /**
     * Singleton constructor.
     */
    private GameSettings() {
        controls = new HashMap<Control, Integer>();
        controls.put(Control.MOVE_FOREWARD, Keyboard.KEY_W);
        controls.put(Control.MOVE_BACKWARD, Keyboard.KEY_S);
        controls.put(Control.TURN_LEFT, Keyboard.KEY_A);
        controls.put(Control.TURN_RIGHT, Keyboard.KEY_D);
        fullscreen = true;
    }
    /**
     * Return the mapping for a control.
     * @param c Control.
     * @return Key mapping
     */
    public int getKeyMapping(final Control c) {
        return controls.get(c);
    }
    /**
     * @return fullscreen
     */
    public boolean isFullscreen() {
        return fullscreen;
    }
}
