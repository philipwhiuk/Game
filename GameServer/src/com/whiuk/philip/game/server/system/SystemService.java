package com.whiuk.philip.game.server.system;

import com.whiuk.philip.game.serverShared.Connection;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage.SystemData;

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
     * @throws InvalidMappingException
     */
    ClientInfo getClientInfo(Connection connection)
            throws InvalidMappingException;

    /**
     * @param clientInfo
     */
    void handleClientDisconnected(ClientInfo clientInfo);

}
