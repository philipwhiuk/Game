package com.whiuk.philip.game.client;

import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * Handles system messages
 * 
 * @author Philip Whitehouse
 */
public interface SystemMessageHandler extends MessageHandler {
    /**
     * Handle a system message
     * 
     * @param message
     */
    void handleSystemMessage(ServerMessage message);

}
