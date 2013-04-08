package com.whiuk.philip.game.client;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMouse;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Start screen.
 * 
 * @author Philip Whitehouse
 */
public class StartScreen implements ScreenController {
    private Nifty nifty;

    public void bind(final Nifty newNifty, final Screen newScreen) {
        this.nifty = newNifty;
    }

    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void quit() {
    }

    public String getStartScreen() {
        return "start";
    }

    public String getMainXML() {
        return "helloworld/helloworld.xml";
    }

    public String getTitle() {
        return "Nifty Hello World";
    }

    public void prepareStart(Nifty nifty) {
        // get the NiftyMouse interface that gives us access to all mouse cursor
        // related stuff
        NiftyMouse niftyMouse = nifty.getNiftyMouse();

        // we could set the position like so
        niftyMouse.setMousePosition(20, 20);
    }
}
