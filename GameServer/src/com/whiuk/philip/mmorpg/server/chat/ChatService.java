package com.whiuk.philip.mmorpg.server.chat;

import com.whiuk.philip.mmorpg.server.auth.AuthEventListener;
import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.ChatData;

/**
 * @author Philip Whitehouse
 */
public interface ChatService extends AuthEventListener {
    /**
     * Process chat message from account.
     * 
     * @param account
     *            Account
     * @param chatData
     *            Chat message
     */
    void processMessage(Account account, ChatData chatData);
    
}
