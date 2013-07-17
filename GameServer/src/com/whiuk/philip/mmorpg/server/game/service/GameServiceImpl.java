package com.whiuk.philip.mmorpg.server.game.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.GameData;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage.GameData.CharacterInformation;
import com.whiuk.philip.mmorpg.server.MessageHandlerService;
import com.whiuk.philip.mmorpg.server.auth.AuthService;
import com.whiuk.philip.mmorpg.server.game.controller.GameCharacterController;
import com.whiuk.philip.mmorpg.server.game.controller.ZoneController;
import com.whiuk.philip.mmorpg.server.game.domain.GameWorld;
import com.whiuk.philip.mmorpg.server.game.domain.PlayerCharacter;
import com.whiuk.philip.mmorpg.server.game.domain.Race;
import com.whiuk.philip.mmorpg.server.game.repository.PlayerCharacterDAO;
import com.whiuk.philip.mmorpg.server.game.repository.RaceDAO;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;
import com.whiuk.philip.mmorpg.server.system.InvalidMappingException;
import com.whiuk.philip.mmorpg.server.watchdog.WatchdogService;
import com.whiuk.philip.mmorpg.serverShared.Account;

/**
 * Game Service implementation.
 * 
 * @author Philip Whitehouse
 */
@Service
public class GameServiceImpl implements GameService {
    /**
	 *
	 */
    private static final Logger LOGGER = Logger
            .getLogger(GameServiceImpl.class);
    /**
     * Authentication service.
     */
    @Autowired
    private AuthService authService;
    /**
     * Message handler.
     */
    @Autowired
    private MessageHandlerService messageHandlerService;
    /**
     * Watchdog service.
     */
    @Autowired
    private WatchdogService watchdogService;
    
    @Autowired
    private GameCharacterController charController;

    @Autowired
    private ZoneController zoneController;
    
    /**
     * Character -> Account mapping.
     */
    private Map<PlayerCharacter, Account> accounts;
    /**
     * Account -> Character mapping.
     */
    private Map<Account, PlayerCharacter> characters;

    /**
     * Game world.
     */
    private GameWorld gameWorld;
    /**
     * 
     */
    private Random random;
    /**
     * 
     */
    @Autowired
    private PlayerCharacterDAO playerCharacterDAO;
    /**
     * 
     */
    @Autowired
    private RaceDAO raceDAO;

    /**
     * Initialize the service.
     */
    @PostConstruct
    public final void init() {
        gameWorld = GameWorld.load();
        characters = new HashMap<Account, PlayerCharacter>();
        accounts = new HashMap<PlayerCharacter, Account>();
        watchdogService.monitor(gameWorld);
        authService.registerAuthEventListener(this);
    }

    @Override
    public void notifyLogin(final Account account) {
        provideCharacters(account);

    }

    /**
     * Provide the account with a list of their characters.
     * @param account Account
     */
    private void provideCharacters(final Account account) {
        List<PlayerCharacter> pcs = playerCharacterDAO.findByAccount(account);
        ServerMessage.GameData.Builder gdb =
                ServerMessage.GameData.newBuilder();
        gdb.setType(ServerMessage.GameData.Type.CHARACTER_SELECTION);
        for (PlayerCharacter pc : pcs) {
                gdb.addCharacterInformation(
                        ServerMessage.GameData.CharacterInformation.newBuilder()
                        .setId(pc.getId().intValue())
                        .setName(pc.getName())
                        .setLocation("Home") //TODO: Get Location
                        .setRace(pc.getRace().getName()).build());
        }
        ServerMessage message = ServerMessage
                .newBuilder()
                .setClientInfo(authService.getConnection(account)
                        .getClientInfo())
                .setType(ServerMessage.Type.GAME)
                .setGameData(gdb)
                    .build();
        messageHandlerService.queueOutboundMessage(message);
    }

    @Override
    public final void processMessage(
            final Account account, final ClientMessage.GameData data) {
        switch(data.getType()) {
            case CHARACTER_CREATION:
                characterCreation(account, data);
                break;
            case CHARACTER_SELECTED:
                characterSelected(account, data);
                break;
            case EXIT:
                if (accounts.get(account) != null) {
                    handleExit(account);
                }
                break;
            default:
                if (characters.get(account) != null) {
                    update(account, data);
                } else {
                    handleActionInInvalidState(account, data);
                }
                break;
        }
    }
    
    /**
     * Handle character creation messages.
     * @param account Account
     * @param data
     */
    private void characterCreation(Account account, GameData data) {
        LOGGER.info("Character creation");
        if (characters.get(account) != null) {
            ServerMessage message = ServerMessage
                .newBuilder()
                .setClientInfo(authService.getConnection(account)
                        .getClientInfo())
                .setType(ServerMessage.Type.GAME)
                .setGameData(
                ServerMessage.GameData
                        .newBuilder()
                        .setError(
                        ServerMessage.
                GameData.Error.CHARACTER_ALREADY_SELECTED))
                    .build();
            messageHandlerService.queueOutboundMessage(message);
        } else if (!data.hasCharacterInformation()
                || !data.getCharacterInformation().hasName()
                || !data.getCharacterInformation().hasRace()) {
            LOGGER.info("Client sent character creation request"
                    + " with missing data.");
            ServerMessage message = ServerMessage
                    .newBuilder()
                    .setClientInfo(authService.getConnection(account)
                            .getClientInfo())
                    .setType(ServerMessage.Type.GAME)
                    .setGameData(
                    ServerMessage.GameData
                            .newBuilder()
                            .setError(
                            ServerMessage.
                    GameData.Error.MISSING_DATA))
                        .build();
                messageHandlerService.queueOutboundMessage(message);
        } else {
            Race r = raceDAO.findByName(data.getCharacterInformation().getRace());
            if (r == null || !r.isPlayable()) {
                ServerMessage message = ServerMessage
                        .newBuilder()
                        .setClientInfo(authService.getConnection(account)
                                .getClientInfo())
                        .setType(ServerMessage.Type.GAME)
                        .setGameData(
                        ServerMessage.GameData
                                .newBuilder()
                                .setError(
                                ServerMessage.
                        GameData.Error.INVALID_DATA))
                            .build();
                messageHandlerService.queueOutboundMessage(message);
            } else {
                createCharacter(account, 
                        data.getCharacterInformation().getName(), r);
            }

        }
    }
    /**
     * @param account
     * @param name
     * @param race
     */
    private void createCharacter(
            final Account account, final String name, final Race race) {
        LOGGER.info("Creating character");
        HibernateUtils.beginTransaction();
        PlayerCharacter c = new PlayerCharacter(account, name, race);
        playerCharacterDAO.save(c);
        HibernateUtils.commitTransaction();
        ServerMessage message = ServerMessage
        .newBuilder()
        .setClientInfo(authService.getConnection(account)
                .getClientInfo())
        .setType(ServerMessage.Type.GAME)
        .setGameData(
        ServerMessage.GameData
            .newBuilder()
            .setType(ServerMessage.GameData.Type.CHARACTER_CREATED)
            .addCharacterInformation(
                    CharacterInformation.newBuilder()
                    .setId(c.getId().intValue())
                    .setName(c.getName())
                    .setLocation("Home") //TODO: Get location
                    .setRace(race.getName()))
                    .build())
        .build();
        messageHandlerService.queueOutboundMessage(message);
    }

    /**
     * Handle character selection messages.
     * @param account Account
     * @param data
     */
    private void characterSelected(
            final Account account, final ClientMessage.GameData data) {
        if (characters.get(account) != null) {
            ServerMessage message = ServerMessage
                .newBuilder()
                .setClientInfo(authService.getConnection(account)
                        .getClientInfo())
                .setType(ServerMessage.Type.GAME)
                .setGameData(
                ServerMessage.GameData
                        .newBuilder()
                        .setError(
                        ServerMessage.
                GameData.Error.CHARACTER_ALREADY_SELECTED))
                    .build();
            messageHandlerService.queueOutboundMessage(message);
        } else {
            loadCharacter(account, data);
        }
    }

    /**
     * Load selected character.
     * @param account Account
     */
    private void loadCharacter(final Account account, ClientMessage.GameData data) {
        HibernateUtils.beginTransaction();
        PlayerCharacter pc = playerCharacterDAO.findByID((long) data.getCharacterInformation().getId());
        if (pc == null) {
            LOGGER.info("Client sent character selection for non-existent character.");
            ServerMessage message = ServerMessage
                    .newBuilder()
                    .setClientInfo(authService.getConnection(account)
                            .getClientInfo())
                    .setType(ServerMessage.Type.GAME)
                    .setGameData(
                    ServerMessage.GameData
                            .newBuilder()
                            .setError(
                            ServerMessage.
                    GameData.Error.INVALID_DATA))
                        .build();
            messageHandlerService.queueOutboundMessage(message);
            return;
        } else if (!pc.getAccount().equals(account)) {
            LOGGER.info("Client sent character selection for character not belonging to them.");
            ServerMessage message = ServerMessage
                    .newBuilder()
                    .setClientInfo(authService.getConnection(account)
                            .getClientInfo())
                    .setType(ServerMessage.Type.GAME)
                    .setGameData(
                    ServerMessage.GameData
                            .newBuilder()
                            .setError(
                            ServerMessage.
                    GameData.Error.INVALID_DATA))
                        .build();
            messageHandlerService.queueOutboundMessage(message);
            return;
        } else {
            accounts.put(pc, account);
            characters.put(account, pc);
            ServerMessage message = ServerMessage
                .newBuilder()
                .setClientInfo(authService.getConnection(account)
                    .getClientInfo())
                .setType(ServerMessage.Type.GAME)
                .setGameData(
                ServerMessage.GameData
                    .newBuilder()
                    .setType(ServerMessage.GameData.Type.ENTER_GAME)
                    .addCharacterInformation(
                        ServerMessage.GameData.CharacterInformation
                        .newBuilder().setName(pc.getName())
                        .setId(pc.getId().intValue())
                        .setRace(pc.getRace().getName())
                        .setLocation("") //TODO Set Location
                        .build())
                    .build())
                .build();
            messageHandlerService.queueOutboundMessage(message);
            zoneController.characterEntered(pc.getZone(), pc);
            return;
        }
    }

    /**
     * @param account
     */
    private void handleExit(final Account account) {
        if (characters.containsKey(account)) {
            charController.handleExit(characters.get(account));
            accounts.remove(characters.remove(account));
        }
    }

    /**
     * Logs action attempted when no character selected.
     * @param account Account
     * @param data Data
     */
    private void handleActionInInvalidState(
            final Account account, final ClientMessage.GameData data) {
        // TODO Auto-generated method stub
    }

    /**
     * Handle client game update.
     * @param account Account
     * @param data
     */
    private void update(final Account account, final ClientMessage.GameData data) {
        switch (data.getType()) {
            case MOVEMENT:
                move(characters.get(account), data.getMovementInformation());
                break;
            case ACTION:
                action(characters.get(account), data.getActionInformation());
                break;
            case COMBAT:
                combat(characters.get(account), data.getCombatInformation());
                break;
            default:
                try {
                    messageHandlerService.handleUnknownMessageType(authService
                            .getConnection(account));
                } catch (InvalidMappingException e) {
                    LOGGER.info("Unknown connection from account " + account
                            + " sent unknown game message type:"
                            + data.getType());
                }
        }
    }

    /**
     * Handles character combat messages.
     * @param character
     * @param combatInformation
     */
    private void combat(final PlayerCharacter character,
            final ClientMessage.GameData.CombatInformation combatInformation) {
        // TODO Auto-generated method stub

    }

    /**
     * Handles character movement messages.
     * @param character
     * @param movementInformation
     */
    private void move(final PlayerCharacter character,
            final ClientMessage.GameData.MovementInformation movementInformation) {
        // TODO Auto-generated method stub

    }

    /**
     * Handles action messages.
     * @param character Character
     * @param actionInformation Action message
     */
    private void action(final PlayerCharacter character,
            final ClientMessage.GameData.ActionInformation actionInformation) {
        switch (actionInformation.getAction()) {
            case TAKE:
                charController.take(character, actionInformation.getSource());
                break;
            case DROP:
                charController.drop(character, actionInformation.getSource());
                break;
            case EXAMINE:
                charController.examine(character,
                        actionInformation.getSource());
                break;
            case EQUIP:
                charController.equip(character, actionInformation.getSource());
                break;
            case UNEQUIP:
                charController.unequip(character,
                        actionInformation.getSource());
                break;
            case CAST:
                charController.cast(character,
                        actionInformation.getSource(),
                        actionInformation.getTarget());
                break;
            case CRAFT:
                charController.craft(character, actionInformation.getSource(),
                        actionInformation.getTarget());
                break;
            case MINE:
                charController.mine(character, actionInformation.getSource(),
                        actionInformation.getTarget());
                break;
            case SMITH:
                charController.smith(character, actionInformation.getSource(),
                        actionInformation.getTarget());
                break;
            case USE:
                charController.use(character, actionInformation.getSource(),
                        actionInformation.getTarget());
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }



    @Override
    public final void notifyLogout(final Account account) {
        if (characters.containsKey(account)) {
            PlayerCharacter c = characters.remove(account);
            accounts.remove(c);
            c.logout();
        }
    }

    @Override
    public final Random getRandom() {
        return random;
    }

}
