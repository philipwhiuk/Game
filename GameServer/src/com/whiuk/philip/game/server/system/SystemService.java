package com.whiuk.philip.game.server.system;

import com.whiuk.philip.game.shared.Messages.ClientMessage.SystemData;
import com.whiuk.philip.game.shared.Messages.ClientInfo;

public interface SystemService {

    void processMessage(SystemData data);

    void processLostConnection(ClientInfo src);

}
