package com.whiuk.philip.mmorpg.client;

import java.util.List;

import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage
    .GameData.CharacterInformation;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Lobby screen.
 * @author Philip Whitehouse
 */
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
     *
     */
    private Account account;
    /**
     *
     */
    private List<CharacterInformation> characters;

    /**
     * @param g
     *            Game client
     * @param a
     *            Account
     */
    public LobbyScreen(final GameClient g, final Account a) {
        this.gameClient = g;
        this.account = a;
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
     * Send message.
     */
    protected final void sendMessage() {
        gameClient.sendChatMessage(textInputMessage
                .getRenderer(TextRenderer.class).getOriginalText());
        textInputMessage.getRenderer(TextRenderer.class).setText("");
    }

    @Override
    public void onEndScreen() {
    }

    /**
     * Handle chat message.
     * @param message Message
     */
    public void handleChatMessage(final ServerMessage message) {
        // TODO Auto-generated method stub

    }

    /**
     * Handle game message.
     * @param message Message
     */
    public final void handleGameMessage(final ServerMessage message) {
        ServerMessage.GameData data = message.getGameData();
        switch (data.getType()) {
            case CHARACTER_SELECTION:
                characters = data.getCharacterInformationList();
                break;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Select a character.
     * @param name Character name
     */
    public final void selectCharacter(final String name) {
        gameClient.sendGameMessage(ClientMessage.GameData.newBuilder()
            .setType(ClientMessage.GameData.Type.CHARACTER_SELECTION)
            .setCharacter(name)
            .build());
    }
}
