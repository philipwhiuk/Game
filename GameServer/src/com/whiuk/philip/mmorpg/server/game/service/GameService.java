package com.whiuk.philip.mmorpg.server.game.service;

import java.util.Random;

import com.whiuk.philip.mmorpg.server.auth.AuthEventListener;
import com.whiuk.philip.mmorpg.server.game.domain.GameCharacter;
import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.GameData;

/**
 * @author Philip
 */
public interface GameService extends AuthEventListener {
    /**
     * Process game message from an account.
     * 
     * @param account Account
     * @param data
     */
    void processMessage(final Account account, final GameData data);

    /**
     * 
     * @return random
     */
    Random getRandom();

    void sendGameData(GameCharacter character, final ServerMessage.GameData message);

}
