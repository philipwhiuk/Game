package com.whiuk.philip.game.server.game;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.whiuk.philip.game.server.MessageHandlerService;
import com.whiuk.philip.game.server.auth.Account;
import com.whiuk.philip.game.server.auth.AuthService;
import com.whiuk.philip.game.shared.Messages.ClientMessage.GameData.ActionInformation;
import com.whiuk.philip.game.shared.Messages.ClientMessage.GameData.CombatInformation;
import com.whiuk.philip.game.shared.Messages.ClientMessage.GameData.MovementInformation;
import com.whiuk.philip.game.shared.Messages.ServerMessage;
import com.whiuk.philip.game.shared.Messages.ServerMessageOrBuilder;
import com.whiuk.philip.game.shared.Messages.ClientMessage.GameData;

@Service
public class GameServiceImpl implements GameService {

	AuthService authService;
	MessageHandlerService messageHandlerService;
	Map<Character, Account> accounts;
	Map<Account, Character> characters;
	
	@Override
    public void processMessage(final Account account, final GameData data) {
		if (data.getType() == GameData.Type.CHARACTER_SELECTION) {
			characterSelection(account, data);
		} else if (data.getType() == GameData.Type.EXIT) {
			if (accounts.get(account) != null) {
				//TODO: Logout
			}
		} else if (characters.get(account) != null) {
			update(account, data);
			
		} else {
			//TODO: Log action attempted when no character selected
		}
    }

	private void update(Account account, GameData data) {
		switch(data.getType()) {
			case MOVEMENT: move(characters.get(account),
					data.getMovementInformation()); break;
			case ACTION: action(characters.get(account),
					data.getActionInformation()); break;
			case COMBAT: combat(characters.get(account),
					data.getCombatInformation()); break;
			default:
				messageHandlerService
					.handleUnknownMessageType(authService
					.getConnection(account));
		}
	}

	/**
	 * Handle character selection messages.
	 * @param account
	 * @param data
	 */
	private void characterSelection(Account account, GameData data) {
		if (characters.get(account) != null) {
			ServerMessage message = ServerMessage
				.newBuilder()
				.setType(ServerMessage.Type.GAME)
				.setGameData(ServerMessage
				.GameData.newBuilder()
				.setError(ServerMessage.GameData
				.Error.CHARACTER_ALREADY_SELECTED))
				.build();
			messageHandlerService.processOutboundMessage(message);
		} else {
			//TODO: Load character
		}
	}

	/**
	 * Handles character combat messages.
	 * @param character
	 * @param combatInformation
	 */
	private void combat(Character character,
			CombatInformation combatInformation) {
		// TODO Auto-generated method stub

	}

	/**
	 * Handles action messages.
	 * @param character
	 * @param actionInformation
	 */
	private void action(Character character,
			ActionInformation actionInformation) {
		// TODO Auto-generated method stub

	}

	/**
	 * Handles character movement messages.
	 * @param character
	 * @param movementInformation
	 */
	private void move(Character character,
			MovementInformation movementInformation) {
		// TODO Auto-generated method stub

	}

}
