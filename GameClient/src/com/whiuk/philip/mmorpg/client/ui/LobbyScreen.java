package com.whiuk.philip.mmorpg.client.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.whiuk.philip.mmorpg.client.Account;
import com.whiuk.philip.mmorpg.client.ChatInterface;
import com.whiuk.philip.mmorpg.client.GameClient;
import com.whiuk.philip.mmorpg.client.GameClientUtils;
import com.whiuk.philip.mmorpg.client.GameInterface;
import com.whiuk.philip.mmorpg.client.LobbyCharacterData;
import com.whiuk.philip.mmorpg.client.GameClient.State;
import com.whiuk.philip.mmorpg.client.LobbyCharacterData.Race;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.ChatData;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.chatcontrol.ChatControl;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Lobby screen.
 * @author Philip Whitehouse
 */
public class LobbyScreen
    implements ScreenController, ChatInterface, GameInterface {
    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(LobbyScreen.class);
    /**
     * Nifty GUI.
     */
    @SuppressWarnings("unused")
    private Nifty nifty;
    /**
     * Game client.
     */
    private GameClient gameClient;
    /**
     * Account data.
     */
    @SuppressWarnings("unused")
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
     * Chat box element.
     */
    private Element chatElement;
    /**
     * Drop down Character List element.
     */
    private Element characterListElement;
    /**
     * Create Character Button.
     */
    @SuppressWarnings("unused")
    private Element createCharacterButton;
    /**
     * Play button.
     */
    @SuppressWarnings("unused")
    private Element playButton;
    /**
     * Tab 1.
     */
    @SuppressWarnings("unused")
    private Element tab1;
    /**
     * Tab 2.
     */
    @SuppressWarnings("unused")
    private Element tab2;
    /**
     * Race Drop Down List Element.
     */
    private Element raceListElement;
    /**
     * Name Input Element.
     */
    private Element nameInputElement;
    /**
     * Character Drop Down.
     */
    private DropDown<LobbyCharacterData> characterListDropDown;
    /**
     * Race Drop Down.
     */
    private DropDown<LobbyCharacterData.Race> raceListDropDown;

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

    @SuppressWarnings("unchecked")
    @Override
    public final void bind(final Nifty newNifty, final Screen screen) {
        this.nifty = newNifty;
        tab1 = screen.findElementByName("tab_1");
        tab2 = screen.findElementByName("tab_2");
        chatElement = screen.findElementByName("chatId");
        characterListElement = screen.findElementByName("character_drop_down");
        characterListDropDown = characterListElement
                .getNiftyControl(DropDown.class);
        raceListElement = screen.findElementByName("race-drop_down");
        nameInputElement = screen.findElementByName("name-input");
        playButton = screen.findElementByName("play_button");
        raceListDropDown = raceListElement
                .getNiftyControl(DropDown.class);
        for (LobbyCharacterData.Race r: LobbyCharacterData.Race.values()) {
            raceListDropDown.addItem(r);
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
    public final void handleChatData(final ServerMessage.ChatData chatData) {
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
    public final void handleGameData(final ServerMessage.GameData gameData) {
        if (gameData.hasError()) {
           LOGGER.info("Game Error: " + gameData.getError());
        } else {
            switch (gameData.getType()) {
                case CHARACTER_CREATED:
                    LOGGER.info("Recieved character created message");
                    for (ServerMessage.GameData.CharacterInformation c
                            : gameData.getCharacterInformationList()) {
                        LobbyCharacterData pc = new LobbyCharacterData(
                                c.getId(), c.getName(),
                                Race.valueOf(c.getRace()),
                                c.getLocation());
                        characters.add(pc);
                        characterListDropDown.addItem(pc);
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
                    characterListDropDown.addItem(pc);
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
                .getNiftyControl(DropDown.class).getSelection();
        if (selection != null) {
            String name = ((LobbyCharacterData) selection).getName();
            int id = ((LobbyCharacterData) selection).getId();
            LOGGER.trace("Player character selected: " + name);
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
                .getNiftyControl(DropDown.class).getSelection();
        String race = ((Race) selection).name();
        String name = nameInputElement.getNiftyControl(TextField.class)
                .getRealText();
        GameClientUtils.sendGameData(ClientMessage.GameData.newBuilder()
                .setType(ClientMessage.GameData.Type.CHARACTER_CREATION)
                .setCharacterInformation(
                        ClientMessage.GameData.CharacterInformation.newBuilder()
                .setName(name)
                .setRace(race).build())
                .build());
    }
    /**
    *
    */
   public final void logout() {
       gameClient.handleLogout();
   }
}
