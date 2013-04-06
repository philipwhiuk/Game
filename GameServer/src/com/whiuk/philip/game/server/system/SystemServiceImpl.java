package com.whiuk.philip.game.server.system;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage.SystemData;

@Service
public class SystemServiceImpl implements SystemService {

	/**
	 * Class logger
	 */
	private static final Logger LOGGER =
			Logger.getLogger(SystemServiceImpl.class);

	@Override
	public void processMessage(final SystemData data) {
		LOGGER.info("Recieved system message");
	}

	@Override
	public void processLostConnection(final ClientInfo src) {
		// TODO Auto-generated method stub

	}

}
