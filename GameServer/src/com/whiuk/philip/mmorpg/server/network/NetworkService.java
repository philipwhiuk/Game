package com.whiuk.philip.mmorpg.server.network;

import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * @author Philip
 */
public interface NetworkService {

    /**
     * Send a message from the server.
     * 
     * @param message
     *            The message
     */
    void processMessage(ServerMessage message);

}
