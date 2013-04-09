package com.whiuk.philip.game.client;

import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * Handles authentication messages.
 * 
 * @author Philip Whitehouse
 */
public interface AuthMessageHandler extends MessageHandler {

    /**
     * Handles an authentication message.
     * 
     * @param message
     */
    void handleAuthMessage(ServerMessage message);

}
