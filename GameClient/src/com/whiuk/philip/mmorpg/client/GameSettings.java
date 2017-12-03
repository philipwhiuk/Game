package com.whiuk.philip.mmorpg.client;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

/**
 * Game Settings.
 * @author Philip
 *
 */
public final class GameSettings {
	
	private static final Logger logger = Logger.getLogger(GameSettings.class);
    /**
     * @return settings
     */
    public static GameSettings getSettings() {
    		Properties props = new Properties();
    		try {
			props.load(new FileReader("settings.properties"));
		} catch (FileNotFoundException e) {
			logger.warn("Settings file not found - using defaults");
		} catch (IOException e) {
			logger.warn("Failed to load settings file - using defaults");
		}
        return new GameSettings(props);
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
     * Whether the client should be in fullscreen mode or not.
     */
    private boolean fullscreen;
    /**
     * Singleton constructor.
     */
    private GameSettings(Properties props) {
        controls = new HashMap<Control, Integer>();
        controls.put(Control.MOVE_FOREWARD, getKeyForControl(props, Control.MOVE_FOREWARD, Keyboard.KEY_W));
        controls.put(Control.MOVE_BACKWARD, getKeyForControl(props, Control.MOVE_BACKWARD, Keyboard.KEY_S));
        controls.put(Control.TURN_LEFT, getKeyForControl(props, Control.TURN_LEFT, Keyboard.KEY_A));
        controls.put(Control.TURN_RIGHT, getKeyForControl(props, Control.TURN_RIGHT, Keyboard.KEY_D));
        fullscreen = Boolean.parseBoolean(props.getProperty("FULLSCREEN", "true"));
    }
    
    private Integer getKeyForControl(Properties props, Control control, Integer defaultKey) {
    		return Integer.parseInt(props.getProperty(control.name(), defaultKey.toString()));
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
