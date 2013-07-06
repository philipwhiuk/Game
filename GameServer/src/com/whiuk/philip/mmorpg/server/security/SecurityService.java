package com.whiuk.philip.mmorpg.server.security;

import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;

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
