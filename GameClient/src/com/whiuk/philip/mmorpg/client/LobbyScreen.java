package com.whiuk.philip.mmorpg.client;

import java.util.List;

import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.ChatData;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage
    .GameData.CharacterInformation;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.textfield.TextFieldControl;
import de.lessvoid.nifty.elements.Element;
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
    }

    @Override
    public final void onStartScreen() {
    }

    /**
     * Send message.
     */
    protected final void sendMessage() {
        //TODO: Get Message
        gameClient.sendChatData(
            ChatData.newBuilder()
            .setMessage("")
            .build());
        //textInputMessage.getControl(TextFieldControl.class).setText("");
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
        gameClient.sendGameData(ClientMessage.GameData.newBuilder()
            .setType(ClientMessage.GameData.Type.CHARACTER_SELECTION)
            .setCharacter(name)
            .build());
    }
}
