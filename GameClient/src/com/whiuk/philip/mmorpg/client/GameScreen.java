package com.whiuk.philip.mmorpg.client;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * In-Game UI.
 * @author Philip
 */
public class GameScreen implements ScreenController {
    
    private GameClient client;
    private Game game;
    private Nifty nifty;

    public GameScreen(GameClient c, Game g) {
        this.client = c;
        this.game = g;
    }

    @Override
    public final void bind(final Nifty newNifty, final Screen screen) {
        this.nifty = newNifty;
        
    }

    @Override
    public void onStartScreen() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onEndScreen() {
        // TODO Auto-generated method stub
        
    }

}
