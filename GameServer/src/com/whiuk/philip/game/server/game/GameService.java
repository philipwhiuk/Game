package com.whiuk.philip.game.server.game;

import com.whiuk.philip.game.server.auth.Account;
import com.whiuk.philip.game.shared.Messages.ClientMessage.GameData;

/**
 * @author Philip
 *
 */
public interface GameService {

    /**
     * Process game message from account
     * @param account
     * @param data
     */
    void processMessage(final Account account, final GameData data);

}
