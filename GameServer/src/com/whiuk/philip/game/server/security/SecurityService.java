package com.whiuk.philip.game.server.security;

import com.whiuk.philip.game.shared.Messages.ClientMessage;

/**
 * @author Philip
 */
public interface SecurityService {

    /**
     * Handle game messages received from clients that aren't authenticated.
     * 
     * @param message
     *            Message received
     */
    void handleMessageFromUnauthenticatedClient(ClientMessage message);

}
