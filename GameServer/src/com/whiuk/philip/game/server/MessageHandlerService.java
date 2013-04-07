package com.whiuk.philip.game.server;

import com.whiuk.philip.game.server.system.Connection;
import com.whiuk.philip.game.server.system.InvalidMappingException;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * Handles message flow.
 * 
 * @author Philip Whitehouse
 */
public interface MessageHandlerService {
    /**
     * @param message
     *            Message to recieve
     */
    void queueInboundMessage(ClientMessage message);

    /**
     * @param message
     *            Message to transmit
     */
    void queueOutboundMessage(ServerMessage message);

    /**
     * Handles messages of an unknown type.
     * 
     * @param clientInfo
     *            Client information for message
     */
    void handleUnknownMessageType(final ClientInfo clientInfo);

    /**
     * Handles messages of an unknown type.
     * 
     * @param connection
     *            Connection information for message
     * @throws InvalidMappingException
     *             Indicates the connection doesn't map to a client.
     */
    void handleUnknownMessageType(Connection connection)
            throws InvalidMappingException;

}
