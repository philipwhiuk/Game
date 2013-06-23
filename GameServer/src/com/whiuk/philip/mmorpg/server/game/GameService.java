package com.whiuk.philip.mmorpg.server.game;

import com.whiuk.philip.game.serverShared.Account;
import com.whiuk.philip.game.shared.Messages.ClientMessage.GameData;

/**
 * @author Philip
 */
public interface GameService {
    /**
     * Process game message from accounts.
     * 
     * @param account
     * @param data
     */
    void processMessage(final Account account, final GameData data);

    /**
     * @param account
     */
    void notifyLogout(Account account);

}
