package com.whiuk.philip.game.server.network;

import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * @author Philip
 *
 */
public interface NetworkService {

    void processMessage(ServerMessage message);

}
