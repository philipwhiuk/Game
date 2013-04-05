package com.whiuk.philip.game.server;

import com.whiuk.philip.game.server.network.Connection;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * Handles message flow
 * @author Philip Whitehouse
 *
 */
public interface MessageHandlerService {
	/**
     * @param message
     */
    void processInboundMessage(final ClientMessage message);
	/**
     * @param message Message to transmit
     */
    void processOutboundMessage(final ServerMessage message);
    /**
     * Handles messages of an unknown type.
     * @param clientInfo Client information for message
     */
    public void handleUnknownMessageType(final ClientInfo clientInfo);
    /**
     * Handles messages of an unknown type.
     * @param clientInfo Client information for message
     */
	public void handleUnknownMessageType(Connection connection);
}
