package com.whiuk.philip.game.server.network;

import com.whiuk.philip.game.shared.Message;

/**
 * @author Philip
 *
 */
public interface NetworkService {

    void processMessage(Message message);

}
