package com.whiuk.philip.mmorpg.client;

import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;

/**
 * Indicates the interface is able to handle chat messages.
 * @author Philip
 *
 */
public interface ChatInterface {
    /**
     * Handle a chat message.
     * @param chatData
     */
    void handleChatMessage(final ServerMessage.ChatData chatData);
}
