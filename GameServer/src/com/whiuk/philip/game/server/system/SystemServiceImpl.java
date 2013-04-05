package com.whiuk.philip.game.server.system;

import org.springframework.stereotype.Service;

import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage.SystemData;

@Service
public class SystemServiceImpl implements SystemService {

	@Override
	public void processMessage(SystemData data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processLostConnection(ClientInfo src) {
		// TODO Auto-generated method stub

	}

}
