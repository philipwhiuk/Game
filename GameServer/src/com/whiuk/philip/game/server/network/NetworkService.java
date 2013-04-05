package com.whiuk.philip.game.server.network;

import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * @author Philip
 *
 */
public interface NetworkService {

   /**
	 * Send a message from the server.
	 * @param message The message
	 */
    void processMessage(ServerMessage message);
    /**
     * Create a Client Info object for a connection
     * @param connection
     * @return
     */
    Connection getClientInfo(Connection connection);

}
