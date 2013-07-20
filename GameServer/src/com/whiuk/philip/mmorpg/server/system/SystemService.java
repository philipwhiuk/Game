package com.whiuk.philip.mmorpg.server.system;

import com.whiuk.philip.mmorpg.serverShared.Connection;
import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.SystemData;

/**
 * @author Philip Whitehouse
 */
public interface SystemService {
    /**
     * @param client
     *            Client info
     * @param data
     *            System data
     */
    void processMessage(ClientInfo client, SystemData data);

    /**
     * @param src Source
     */
    void processLostConnection(ClientInfo src);

    /**
     * @param clientInfo Client Info
     * @return Connection
     */
    Connection getConnection(ClientInfo clientInfo);

    /**
     * @param connection Connection
     * @return Client Info
     * @throws InvalidMappingException Exception
     */
    ClientInfo getClientInfo(Connection connection)
            throws InvalidMappingException;

    /**
     * @param clientInfo Client Info
     */
    void handleClientDisconnected(ClientInfo clientInfo);

}
