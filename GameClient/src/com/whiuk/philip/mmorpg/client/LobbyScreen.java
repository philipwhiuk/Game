package com.whiuk.philip.mmorpg.client;

import java.util.List;

import org.apache.log4j.Logger;

import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.ChatData;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage
    .GameData.CharacterInformation;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.chatcontrol.ChatControl;
import de.lessvoid.nifty.controls.dropdown.DropDownControl;
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
     * Class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LobbyScreen.class);
    /**
     * Nifty GUI
     */
    private Nifty nifty;
    /**
     * Game client
     */
    private GameClient gameClient;
    /**
     * Account data
     */
    private Account account;
    /**
     * List of characters.
     */
    private List<CharacterInformation> characters;
    /**
     * Chat box element
     */
    private Element chatElement;
    /**
     * Drop down Character List element
     */
    private Element characterListElement;
    /**
     * Create Character Button
     */
    private Element createCharacterButton;
    /**
     * Play button
     */
    private Element playButton;

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
        chatElement = screen.findElementByName("chatId");
        characterListElement = screen.findElementByName("character_drop_down");
        playButton = screen.findElementByName("play_button");
    }

    @Override
    public final void onStartScreen() {
    }

    /**
     * Send message.
     * @param id ID
     * @param event Event
     */
    @NiftyEventSubscriber(id = "chatId")
    public final void onChatTextSendEvent(
            final String id, final ChatTextSendEvent event) {
        gameClient.sendChatData(
                ChatData.newBuilder()
                .setPrivate(false)
                .setChannel(0)
                .setMessage(event.getText())
                .build());
    }

    @Override
    public void onEndScreen() {
    }

    /**
     * Handle chat message.
     * @param chatData Chat Data
     */
    public final void handleChatMessage(final ServerMessage.ChatData chatData) {
        switch(chatData.getType()) {
            case PLAYER_JOINED:
                chatElement.getControl(ChatControl.class)
                    .addPlayer(chatData.getSource(), null);
                break;
            case PLAYER_LEFT:
                chatElement.getControl(ChatControl.class)
                    .removePlayer(chatData.getSource());
                break;
            case MESSAGE:
                String text = chatData.getMessage();
                chatElement.getControl(ChatControl.class)
                    .receivedChatLine(text, null);
                break;
            default:
                LOGGER.error("Unhandled chat message");
                break;
        }
    }

    /**
     * Handle game message.
     * @param gameData Game Data
     */
    public final void handleGameMessage(final ServerMessage.GameData gameData) {
        switch (gameData.getType()) {
            case CHARACTER_SELECTION:
                LOGGER.info("Recieved character selection message");
                characters = gameData.getCharacterInformationList();
                for (CharacterInformation c: characters) {
                    characterListElement
                        .getControl(DropDownControl.class).addItem(c);
                }
                break;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Play with the selected character.
     */
    public final void play() {
        Object selection = characterListElement
                .getControl(DropDownControl.class).getSelection();
        if (selection != null) {
            String name = ((CharacterInformation) selection).getName();
            gameClient.sendGameData(ClientMessage.GameData.newBuilder()
                    .setType(ClientMessage.GameData.Type.CHARACTER_SELECTION)
                    .setCharacter(name)
                    .build());
        }
    }
}
