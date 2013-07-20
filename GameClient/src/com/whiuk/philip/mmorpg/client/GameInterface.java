package com.whiuk.philip.mmorpg.client;

import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;

/**
 * Indicates the interface is able to handle game messages.
 * @author Philip
 *
 */
interface GameInterface {
    /**
     * Handle a game message.
     * @param gameData Game data
     */
    void handleGameData(final ServerMessage.GameData gameData);
}
