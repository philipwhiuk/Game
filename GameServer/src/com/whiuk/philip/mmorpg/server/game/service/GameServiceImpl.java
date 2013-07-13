package com.whiuk.philip.mmorpg.server.game.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.shared.Messages.
    ClientMessage.GameData;
import com.whiuk.philip.mmorpg.shared.Messages.
    ClientMessage.GameData.ActionInformation;
import com.whiuk.philip.mmorpg.shared.Messages.
    ClientMessage.GameData.CombatInformation;
import com.whiuk.philip.mmorpg.shared.Messages.
    ClientMessage.GameData.MovementInformation;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.server.MessageHandlerService;
import com.whiuk.philip.mmorpg.server.auth.AuthService;
import com.whiuk.philip.mmorpg.server.game.controller.GameCharacterController;
import com.whiuk.philip.mmorpg.server.game.controller.ZoneController;
import com.whiuk.philip.mmorpg.server.game.domain.GameWorld;
import com.whiuk.philip.mmorpg.server.game.domain.Item;
import com.whiuk.philip.mmorpg.server.game.domain.PlayerCharacter;
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
    public final void processMessage(
            final Account account, final GameData data) {
        if (data.getType() == GameData.Type.CHARACTER_SELECTION) {
            characterSelection(account, data);
        } else if (data.getType() == GameData.Type.EXIT) {
            if (accounts.get(account) != null) {
                handleExit(account);
            }
        } else if (characters.get(account) != null) {
            update(account, data);
        } else {
            handleActionInInvalidState(account, data);
        }
    }

    /**
     * @param account
     */
    private void handleExit(final Account account) {
        // TODO Auto-generated method stub
    }

    /**
     * Logs action attempted when no character selected.
     * @param account Account
     * @param data Data
     */
    private void handleActionInInvalidState(
            final Account account, final GameData data) {
        // TODO Auto-generated method stub
    }

    /**
     * Handle client game update.
     * @param account Account
     * @param data
     */
    private void update(final Account account, final GameData data) {
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
     * Handle character selection messages.
     * @param account Account
     * @param data
     */
    private void characterSelection(
            final Account account, final GameData data) {
        if (characters.get(account) != null) {
            ServerMessage message = ServerMessage
                .newBuilder()
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
            loadCharacters(account);
        }
    }

    /**
     * Load character.
     * @param account Account
     */
    private void loadCharacters(final Account account) {
        // TODO Auto-generated method stub
    }

    /**
     * Handles character combat messages.
     * @param character
     * @param combatInformation
     */
    private void combat(final PlayerCharacter character,
            final CombatInformation combatInformation) {
        // TODO Auto-generated method stub

    }

    /**
     * Handles character movement messages.
     * @param character
     * @param movementInformation
     */
    private void move(final PlayerCharacter character,
            final MovementInformation movementInformation) {
        // TODO Auto-generated method stub

    }

    /**
     * Handles action messages.
     * @param character Character
     * @param actionInformation Action message
     */
    private void action(final PlayerCharacter character,
            final ActionInformation actionInformation) {
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

    @Override
    public void notifyLogin(Account account) {
        // TODO Auto-generated method stub
        
    }

}
