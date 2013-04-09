package com.whiuk.philip.game.client;

import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * Handles game messages.
 * 
 * @author Philip Whitehouse
 */
public interface GameMessageHandler extends MessageHandler {

    /**
     * Handle game message.
     * 
     * @param message
     */
    void handleGameMessage(ServerMessage message);

}
