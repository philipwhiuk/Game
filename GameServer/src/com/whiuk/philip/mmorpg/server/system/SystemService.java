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
     * @param src
     */
    void processLostConnection(ClientInfo src);

    /**
     * @param clientInfo
     * @return
     */
    Connection getConnection(ClientInfo clientInfo);

    /**
     * @param connection
     * @return
     * @throws InvalidMappingException Exception
     */
    ClientInfo getClientInfo(Connection connection)
            throws InvalidMappingException;

    /**
     * @param clientInfo
     */
    void handleClientDisconnected(ClientInfo clientInfo);

}
