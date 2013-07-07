package com.whiuk.philip.mmorpg.server.game;

import java.util.Random;

import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.GameData;

/**
 * @author Philip
 */
public interface GameService {
    /**
     * Process game message from an account.
     * 
     * @param account Account
     * @param data
     */
    void processMessage(final Account account, final GameData data);

    /**
     * @param account Account
     */
    void notifyLogout(Account account);

    /**
     * 
     * @return random
     */
    Random getRandom();

}
