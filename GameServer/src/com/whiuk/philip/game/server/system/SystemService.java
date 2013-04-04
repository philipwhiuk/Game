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
	 *
	 * @param data
	 */
    void processMessage(SystemData data);
    /**
     *
     * @param src
     */
    void processLostConnection(ClientInfo src);

}
