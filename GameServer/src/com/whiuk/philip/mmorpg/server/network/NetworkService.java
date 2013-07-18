package com.whiuk.philip.mmorpg.server.network;

import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;

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
    void sendMessage(ServerMessage message);

    /**
     * @return current number of connections
     */
    long getConnectionCount();

}
