package com.whiuk.philip.mmorpg.client.ui;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.whiuk.philip.mmorpg.client.GameClient;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Login screen.
 * @author Philip Whitehouse
 */
public class SettingsScreen implements ScreenController {
    /**
     * Boolean option with human readable text.
     * @author Philip
     */
    enum BooleanOption {
        /**
         * Yes.
         */
        YES("Yes", true),
        /**
         * No.
         */
        NO("No", false);
        /**
         * Text.
         */
        private String text;
        /**
         * Value.
         */
        private boolean value;
        /**
         * @param t Text
         * @param v Value
         */
        BooleanOption(final String t, final boolean v) {
            this.text = t;
            this.value = v;
        }
        @Override
        public String toString() {
            return text;
        }
        /**
         * @return value
         */
        public boolean value() {
            return value;
        }
    }

    /**
     * Nifty.
     */
    @SuppressWarnings("unused")
    private Nifty nifty;
    /**
     * Fullscreen Drop Down.
     */
    private DropDown<BooleanOption> fullscreenDropDown;
    /**
     * Resolution Drop Down.
     */
    private DropDown<DisplayMode> resolutionDropDown;
    /**
     * Game client.
     */
    private GameClient gameClient;
    /**
     * Fullscreen element.
     */
    private Element fullscreenListElement;
    /**
     * Resolution element.
     */
    private Element resolutionListElement;
    /**
     * V-Sync element.
     */
    private Element vsyncListElement;
    /**
     * V-Sync drop down.
     */
    private DropDown<BooleanOption> vsyncDropDown;
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

    @SuppressWarnings("unchecked")
    @Override
    public final void bind(final Nifty newNifty, final Screen screen) {
        this.nifty = newNifty;

        fullscreenListElement = screen.
                findElementByName("fullscreen_drop_down");
        fullscreenDropDown = fullscreenListElement
                .getNiftyControl(DropDown.class);
        fullscreenDropDown.addAllItems(Arrays.asList(BooleanOption.values()));
        if (Display.isFullscreen()) {
            fullscreenDropDown.selectItem(BooleanOption.YES);
        } else {
            fullscreenDropDown.selectItem(BooleanOption.NO);
        }
        resolutionListElement = screen.
                findElementByName("resolution_drop_down");
        resolutionDropDown = resolutionListElement
                .getNiftyControl(DropDown.class);
        try {
            resolutionDropDown.addAllItems(Arrays.asList(
                    Display.getAvailableDisplayModes()));
            resolutionDropDown.selectItem(Display.getDisplayMode());
        } catch (LWJGLException e) {
            LOGGER.error("Unable to retrieve available display modes", e);
        }

        vsyncListElement = screen.
                findElementByName("vsync_drop_down");
        vsyncDropDown = vsyncListElement
                .getNiftyControl(DropDown.class);
        vsyncDropDown.addAllItems(Arrays.asList(BooleanOption.values()));
    }

    @Override
    public final void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    /**
     * Apply new settings.
     */
    public final void apply() {
        try {
            Display.setFullscreen(fullscreenDropDown.getSelection().value());
            Display.setDisplayMode(resolutionDropDown.getSelection());
            Display.setVSyncEnabled(vsyncDropDown.getSelection().value());
        } catch (LWJGLException e) {
            setMessage(e.getMessage());
            return;
        }
        exit();
    }
    /**
     * Reset.
     */
    public final void reset() {
        //TODO Reset settings
    }

    /**
     * Cancel.
     */
    public final void cancel() {
        exit();
    }
    /**
     * Exit.
     */
    private void exit() {
        switch(gameClient.getState()) {
            case LOBBY:
                gameClient.switchToLobbyScreen();
                break;
            case LOGIN:
                gameClient.switchToLoginScreen();
                break;
            case GAME:
                gameClient.switchToGameScreen();
                break;
            default:
                throw new IllegalStateException();
        }
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
