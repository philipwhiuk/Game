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
    /**
     * Send a message to the <code>target</code> account
     * that the <code>src</code> send a message to the channel.
     * @param id Channel ID
     * @param src Source
     * @param target Target
     * @param message Message
     */
    void sendMessageFromChannel(int id, Account src,
            Account target, String message);
    /**
     * Inform the <code>target</code> account that
     * the <code>src</code> account joined the channel.
     * @param id Channel ID
     * @param src Source
     * @param target Target
     */
    void sendPlayerJoinedChannel(int id, Account src, Account target);
}
