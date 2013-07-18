package com.whiuk.philip.mmorpg.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.whiuk.philip.mmorpg.client.GameClient.State;
import com.whiuk.philip.mmorpg.client.LobbyCharacterData.Race;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.ChatData;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.chatcontrol.ChatControl;
import de.lessvoid.nifty.controls.dropdown.DropDownControl;
import de.lessvoid.nifty.controls.tabs.TabControl;
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
public class LobbyScreen implements ScreenController, ChatInterface {
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
     * <p>NB: Use of {@link LobbyCharacterData} here requires we override
     * {@link LobbyCharacterData#toString()} method to provide the name in the
     * drop-down box. Hence we use a simplified object rather than the full
     * player character data object (also saving on memory).</p>
     */
    private List<LobbyCharacterData> characters;
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
     * Tab 1
     */
    private Element tab1;
    /**
     * Tab 2
     */
    private Element tab2;
    /**
     * Race Drop Down List
     */
    private Element raceListElement;
    /**
     * 
     */
    private Element nameInputElement;

    /**
     * @param g
     *            Game client
     * @param a
     *            Account
     */
    public LobbyScreen(final GameClient g, final Account a) {
        this.gameClient = g;
        this.account = a;
        characters = new ArrayList<LobbyCharacterData>();
    }

    @Override
    public final void bind(final Nifty newNifty, final Screen screen) {
        this.nifty = newNifty;
        tab1 = screen.findElementByName("tab_1");
        tab2 = screen.findElementByName("tab_2");
        chatElement = screen.findElementByName("chatId");
        characterListElement = screen.findElementByName("character_drop_down");
        raceListElement = screen.findElementByName("race-drop_down");
        nameInputElement = screen.findElementByName("name-input");
        playButton = screen.findElementByName("play_button");
        for (LobbyCharacterData.Race r: LobbyCharacterData.Race.values()) {
            raceListElement
                .getControl(DropDownControl.class).addItem(r);
        }
        gameClient.setState(State.LOBBY);
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
        GameClientUtils.sendChatData(
                ChatData.newBuilder()
                .setPrivate(false)
                .setChannel(0)
                .setMessage(event.getText())
                .build());
    }

    @Override
    public final void onEndScreen() {
    }

    @Override
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
        if (gameData.hasError()) {
           LOGGER.info("Game Error: " + gameData.getError());
        } else {
            switch (gameData.getType()) {
                case CHARACTER_CREATED:
                    LOGGER.info("Recieved character created message");
                    for (ServerMessage.GameData.CharacterInformation c :
                            gameData.getCharacterInformationList()) {
                        LobbyCharacterData pc = new LobbyCharacterData(
                                c.getId(), c.getName(),
                                Race.valueOf(c.getRace()),
                                c.getLocation());
                        characters.add(pc);
                        characterListElement.getControl(DropDownControl.class)
                            .addItem(pc);
                    }
                    break;
                case CHARACTER_SELECTION:
                    LOGGER.info("Recieved character selection message");
                    ServerMessage.GameData.CharacterInformation c =
                            gameData.getCharacterInformationList().get(0);
                    LobbyCharacterData pc = new LobbyCharacterData(
                            c.getId(), c.getName(),
                            Race.valueOf(c.getRace()),
                            c.getLocation());
                    characters.add(pc);
                    characterListElement.getControl(DropDownControl.class)
                        .addItem(pc);
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    /**
     * Play with the selected character.
     */
    public final void play() {
        Object selection = characterListElement
                .getControl(DropDownControl.class).getSelection();
        if (selection != null) {
            String name = ((LobbyCharacterData) selection).getName();
            int id = ((LobbyCharacterData) selection).getId();
            LOGGER.info("Player character selected: " + name);
            GameClientUtils.sendGameData(ClientMessage.GameData.newBuilder()
                    .setType(ClientMessage.GameData.Type.CHARACTER_SELECTED)
                    .setCharacterInformation(
                            ClientMessage.GameData.CharacterInformation
                            .newBuilder().setId(id).setName(name).build())
                    .build());
        } else {
            LOGGER.info("Null character selected: ");
        }
    }
    /**
     * Create a character.
     */
    public final void createCharacter() {
        Object selection = raceListElement
                .getControl(DropDownControl.class).getSelection();
        String race = ((Race) selection).name();
        String name = nameInputElement.getControl(TextFieldControl.class)
                .getRealText();
        GameClientUtils.sendGameData(ClientMessage.GameData.newBuilder()
                .setType(ClientMessage.GameData.Type.CHARACTER_CREATION)
                .setCharacterInformation(
                        ClientMessage.GameData.CharacterInformation.newBuilder()
                .setName(name)
                .setRace(race).build())
                .build());
    }
}
