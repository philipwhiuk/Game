package com.whiuk.philip.game.server.system;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.game.server.MessageHandlerService;
import com.whiuk.philip.game.server.auth.AuthService;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage.SystemData;

/**
 *
 * @author Philip Whitehouse
 *
 */
@Service
public class SystemServiceImpl implements SystemService {

	/**
	 * Class logger
	 */
	private static final Logger LOGGER =
			Logger.getLogger(SystemServiceImpl.class);
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

	@Override
	public final void processMessage(final ClientInfo clientInfo,
			final SystemData data) {
		switch(data.getType()) {
			case CONNECTED:
				handleConnectionMessage(clientInfo);
				break;
			case DISCONNECTING:
				Connection connection = connections
					.get(clientInfo);
				connection.disconnect();
				authService.notifyDisconnection(connection);
				break;
			default:
				messageHandler.handleUnknownMessageType(
						clientInfo);
		}
	}

	/**
	 *
	 * @param clientInfo
	 */
	private void handleConnectionMessage(final ClientInfo clientInfo) {
		//If the connection exists
		if (connections.containsKey(clientInfo)) {
			LOGGER.info("Connection message from known client.");
			connections.get(clientInfo).connect();
		} else {
			LOGGER.info("New client connected");
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

}
