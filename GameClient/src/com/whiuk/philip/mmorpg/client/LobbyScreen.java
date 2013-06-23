package com.whiuk.philip.mmorpg.client;

import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Lobby screen.
 * 
 * @author Philip Whitehouse
 */
@SuppressWarnings("deprecation")
// TODO: Work out how Nifty 1.3.2 uses controls.
public class LobbyScreen implements ScreenController {
    /**
     *
     */
    private Nifty nifty;
    /**
     *
     */
    private Element textInputMessage;
    /**
    *
    */
    private GameClient gameClient;

    /**
     * @param g
     *            Game client
     */
    public LobbyScreen(final GameClient g) {
        this.gameClient = g;
    }

    @Override
    public final void bind(final Nifty newNifty, final Screen screen) {
        this.nifty = newNifty;
        textInputMessage = screen.findElementByName("text_input_message");
    }

    @Override
    public final void onStartScreen() {
        textInputMessage.addInputHandler(new KeyInputHandler() {
            @Override
            public boolean keyEvent(final NiftyInputEvent inputEvent) {
                if (inputEvent == null) {
                    return false;
                }
                switch (inputEvent) {
                    case SubmitText:
                        sendMessage();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
        textInputMessage.setFocus();
    }

    /**
     *
     */
    protected void sendMessage() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEndScreen() {
    }

    /**
     * Handle chat message
     * 
     * @param message
     */
    public void handleChatMessage(final ServerMessage message) {
        // TODO Auto-generated method stub

    }
}
