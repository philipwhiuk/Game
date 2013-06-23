package com.whiuk.philip.mmorpg.server;

import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.server.system.InvalidMappingException;
import com.whiuk.philip.mmorpg.serverShared.Connection;

/**
 * Handles message flow.
 * 
 * @author Philip Whitehouse
 */
public interface MessageHandlerService extends Runnable {
    /**
     * @param message
     *            Message to receive
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
