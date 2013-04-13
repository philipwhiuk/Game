package com.whiuk.philip.game.server.chat;

import com.whiuk.philip.game.serverShared.Account;
import com.whiuk.philip.game.shared.Messages.ClientMessage.ChatData;

/**
 * @author Philip Whitehouse
 */
public interface ChatService {
    /**
     * Process chat message from account.
     * 
     * @param account
     *            Account
     * @param chatData
     *            Chat message
     */
    void processMessage(Account account, ChatData chatData);

    /**
     * Notify the chat service an account has logged out.
     * 
     * @param account
     */
    void notifyLogout(Account account);

}
