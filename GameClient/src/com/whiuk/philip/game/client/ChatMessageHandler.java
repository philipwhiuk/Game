package com.whiuk.philip.game.client;

import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * Handles chat messages.
 * 
 * @author Philip Whitehouse
 */
public interface ChatMessageHandler extends MessageHandler {
    /**
     * Handle a chat message.
     * 
     * @param message
     */
    void handleChatMessage(ServerMessage message);

}
