package com.whiuk.philip.mmorpg.server.system;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.SystemData;
import com.whiuk.philip.mmorpg.server.MessageHandlerService;
import com.whiuk.philip.mmorpg.server.auth.AuthService;
import com.whiuk.philip.mmorpg.serverShared.Connection;

/**
 * @author Philip Whitehouse
 */
@Service
public class SystemServiceImpl implements SystemService {

    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(SystemServiceImpl.class);
    /**
	 *
	 */
    private Map<ClientInfo, Connection> connections;
    /**
	 *
	 */
    private Map<Connection, ClientInfo> clients;

    /**
	 *
	 */
    @Autowired
    private AuthService authService;
    /**
	 *
	 */
    @Autowired
    private MessageHandlerService messageHandler;

    /**
     * Constructor.
     */
    public SystemServiceImpl() {
        connections = new HashMap<ClientInfo, Connection>();
        clients = new HashMap<Connection, ClientInfo>();
    }

    @Override
    public final void processMessage(final ClientInfo clientInfo,
            final SystemData data) {
        switch (data.getType()) {
            case CONNECTED:
                handleConnectionMessage(clientInfo);
                break;
            case DISCONNECTING:
                LOGGER.info("Client <" + clientInfo + "> disconnected");
                Connection connection = connections.get(clientInfo);
                disconnect(connection);
                break;
            default:
                messageHandler.handleUnknownMessageType(clientInfo);
        }
    }

    /**
     * @param clientInfo
     */
    private void handleConnectionMessage(final ClientInfo clientInfo) {
        // If the connection exists
        if (connections.containsKey(clientInfo)) {
            LOGGER.info("Connection message from known client  <" + clientInfo
                    + "> .");
            connect(connections.get(clientInfo));
        } else {
            LOGGER.info("New client connected  <" + clientInfo + "> ");
            Connection con = new Connection(clientInfo,
                    System.nanoTime(), true);
            connections.put(clientInfo, con);
            clients.put(con, clientInfo);
        }
    }

    @Override
    public void processLostConnection(final ClientInfo src) {
        // TODO Auto-generated method stub

    }

    @Override
    public final Connection getConnection(final ClientInfo clientInfo) {
        return connections.get(clientInfo);
    }

    @Override
    public final ClientInfo getClientInfo(final Connection connection)
            throws InvalidMappingException {
        ClientInfo clientInfo = clients.get(connection);
        if (clientInfo == null) {
            throw new InvalidMappingException("Unknown connection");
        }
        return clientInfo;
    }

    @Override
    public final void handleClientDisconnected(final ClientInfo clientInfo) {
        if (connections.containsKey(clientInfo)) {
            disconnect(connections.get(clientInfo));
        }
    }

    /**
     *
     */
    public final void connect(final Connection con) {
        con.setActive(true);
        con.setLastConnectionTime(System.nanoTime());
    }

    /**
     *
     */
    public final void disconnect(final Connection con) {
        con.setActive(false);
        authService.notifyDisconnection(con);

    }
}
