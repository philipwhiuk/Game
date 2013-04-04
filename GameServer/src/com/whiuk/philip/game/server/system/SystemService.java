package com.whiuk.philip.game.server.system;

import com.whiuk.philip.game.shared.Message;
import com.whiuk.philip.game.shared.Source;

public interface SystemService {

    void processMessage(Message message);

    void processLostConnection(Source src);

}
