package com.whiuk.philip.mmorpg.client;

import org.apache.log4j.Logger;

import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;

/**
 * Common game client functions.
 * @author Philip
 */
final class GameClientUtils {
    /**
     * Class logger.
     */
    private static final transient Logger LOGGER = Logger
            .getLogger(GameClientUtils.class);

    /**
     * Utility class, private constructor.
     */
    private GameClientUtils() {
    }

    /**
     * Helper method to send chat data to server and
     * keep server message handling code from core classes.
     * @param data Chat data to send to server
     */
    static void sendAuthData(final ClientMessage.AuthData data) {
        LOGGER.trace("Sending auth data.");
        GameClient.getGameClient().sendOutboundMessage(ClientMessage
                .newBuilder()
                .setType(ClientMessage.Type.CHAT)
                .setClientInfo(GameClient.getGameClient().getClientInfo())
                .setAuthData(data)
                .build());
    }

    /**
     * Helper method to send chat data to server and
     * keep server message handling code from core classes.
     * @param data Chat data to send to server
     */
    static void sendChatData(final ClientMessage.ChatData data) {
        LOGGER.trace("Sending chat data.");
        GameClient.getGameClient().sendOutboundMessage(ClientMessage
                .newBuilder()
                .setType(ClientMessage.Type.CHAT)
                .setClientInfo(GameClient.getGameClient().getClientInfo())
                .setChatData(data)
                .build());
    }

    /**
     * Helper method to send game data to server and
     * keep server message handling code from core classes.
     * @param data Game data to send to server
     */
    static void sendGameData(final ClientMessage.GameData data) {
        LOGGER.trace("Sending game data.");
        GameClient.getGameClient().sendOutboundMessage(ClientMessage
                .newBuilder()
                .setType(ClientMessage.Type.GAME)
                .setClientInfo(GameClient.getGameClient().getClientInfo())
                .setGameData(data)
                .build());
    }

    /**
     * Helper method to send chat data to server and
     * keep server message handling code from core classes.
     * @param data Chat data to send to server
     */
    static void sendSystemData(final ClientMessage.SystemData data) {
        LOGGER.trace("Sending system data.");
        GameClient.getGameClient().sendOutboundMessage(ClientMessage
                .newBuilder()
                .setType(ClientMessage.Type.SYSTEM)
                .setClientInfo(GameClient.getGameClient().getClientInfo())
                .setSystemData(data)
                .build());
    }
}
