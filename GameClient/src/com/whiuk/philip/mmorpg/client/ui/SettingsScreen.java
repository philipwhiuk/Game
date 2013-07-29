package com.whiuk.philip.mmorpg.client.ui;

import org.apache.log4j.Logger;

import com.whiuk.philip.mmorpg.client.GameClient;
import com.whiuk.philip.mmorpg.client.GameClient.State;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Login screen.
 * @author Philip Whitehouse
 */
public class SettingsScreen implements ScreenController {
    /**
     * Nifty.
     */
    @SuppressWarnings("unused")
    private Nifty nifty;
    /**
     * Username input field.
     */
    private Element textInputUsername;
    /**
     * Password input field.
     */
    private Element textInputPassword;
    /**
     * Game client.
     */
    private GameClient gameClient;
    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(SettingsScreen.class);
    /**
     * @param g
     *            Game client
     */
    public SettingsScreen(final GameClient g) {
        this.gameClient = g;
    }

    @Override
    public final void bind(final Nifty newNifty, final Screen screen) {
        this.nifty = newNifty;
    }

    @Override
    public final void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public final void apply() {
        //TODO: Apply settings
    }
    
    public final void reset() {
        //TODO: Reset settings
    }

    /**
     * Set a message for the user.
     * @param m Message
     */
    public final void setMessage(final String m) {
        LOGGER.info("Setting message: " + m);
        // TODO Auto-generated method stub
    }
}
