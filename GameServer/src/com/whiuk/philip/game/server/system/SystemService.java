package com.whiuk.philip.game.server.system;

import com.whiuk.philip.game.shared.Messages.ClientMessage.SystemData;
import com.whiuk.philip.game.shared.Messages.ClientInfo;

/**
 *
 * @author Philip Whitehouse
 *
 */
public interface SystemService {
	/**
	 * @param client Client info
	 * @param data System data
	 */
	void processMessage(ClientInfo client, SystemData data);
    /**
     *
     * @param src
     */
    void processLostConnection(ClientInfo src);
    /**
     * 
     * @param clientInfo
     * @return
     */
	Connection getConnection(ClientInfo clientInfo);
	/**
	 * 
	 * @param connection
	 * @return
	 * @throws InvalidMappingException 
	 */
	ClientInfo getClientInfo(Connection connection) throws InvalidMappingException;

}
