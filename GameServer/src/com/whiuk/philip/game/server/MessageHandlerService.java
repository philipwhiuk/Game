package com.whiuk.philip.game.server;

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
}
