package com.whiuk.philip.mmorpg.client;

import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage.ChatData;

/**
 * Indicates the interface is able to handle chat messages.
 * @author Philip
 *
 */
public interface ChatInterface {
    /**
     * Handle a chat message.
     * @param chatData Chat data
     */
    void handleChatData(final ChatData chatData);
}
