package com.whiuk.philip.game.server;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.game.server.auth.Account;
import com.whiuk.philip.game.server.auth.AuthService;
import com.whiuk.philip.game.server.chat.ChatService;
import com.whiuk.philip.game.server.game.GameService;
import com.whiuk.philip.game.server.network.NetworkService;
import com.whiuk.philip.game.server.security.SecurityService;
import com.whiuk.philip.game.server.system.Connection;
import com.whiuk.philip.game.server.system.InvalidMappingException;
import com.whiuk.philip.game.server.system.SystemService;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * @author Philip
 */
@Service
public class MessageHandlerServiceImpl implements MessageHandlerService {
    /**
     * Network service.
     */
    @Autowired
    private NetworkService networkService;
    /**
     * Authentication service.
     */
    @Autowired
    private AuthService authService;
    /**
     * Game service.
     */
    @Autowired
    private GameService gameService;
    /**
     * Security service.
     */
    @Autowired
    private SecurityService secService;
    /**
     * System service.
     */
    @Autowired
    private SystemService systemService;
    /**
     * Chat service.
     */
    @Autowired
    private ChatService chatService;

    /**
     * Queue of inbound messages to process.
     */
    private Queue<ClientMessage> inbound;
    /**
     * Queue of outbound messages to transmit.
     */
    private Queue<ServerMessage> outbound;

    /**
     * Amount of time handler tries to sleep when idle.
     */
    private static final int HANDLER_SLEEP_TIME = 100;
    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(MessageHandlerServiceImpl.class);

    /**
     * Whether the message handler is running.
     */
    private boolean running;

    /**
     *
     */
    public MessageHandlerServiceImpl() {
        inbound = new ConcurrentLinkedQueue<ClientMessage>();
        outbound = new ConcurrentLinkedQueue<ServerMessage>();
    }

    @Override
    public final void run() {
        running = true;
        while (running) {
            boolean processed = false;
            ServerMessage serverMessage;
            serverMessage = outbound.poll();
            while (serverMessage != null) {
                sendOutboundMessage(serverMessage);
                serverMessage = outbound.poll();
            }
            ClientMessage clientMessage = inbound.poll();
            if (clientMessage != null) {
                processInboundMessage(clientMessage);
            }
            if (!processed) {
                try {
                    Thread.sleep(HANDLER_SLEEP_TIME);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
     * Process an inbound message
     * 
     * @param message
     */
    private void processInboundMessage(final ClientMessage message) {
        LOGGER.info("Processing inbound message");
        if (message.hasSystemData()) {
            systemService.processMessage(message.getClientInfo(),
                    message.getSystemData());
        }
        if (message.hasAuthData()) {
            authService.processMessage(message.getClientInfo(),
                    message.getAuthData());
        } else {
            Account account = authService.getAccount(message);
            if (account == null) {
                secService.handleMessageFromUnauthenticatedClient(message);
            } else {
                switch (message.getType()) {
                    case GAME:
                        gameService.processMessage(account,
                                message.getGameData());
                        break;
                    case CHAT:
                        chatService.processMessage(
                                authService.getAccount(message),
                                message.getChatData());
                        break;
                    default:
                        handleUnknownMessageType(message.getClientInfo());
                        break;
                }
            }
        }
    }

    @Override
    public final void handleUnknownMessageType(final Connection connection)
            throws InvalidMappingException {
        // TODO Auto-generated method stub
        handleUnknownMessageType(systemService.getClientInfo(connection));
    }

    @Override
    public final void handleUnknownMessageType(final ClientInfo clientInfo) {
        ServerMessage response = ServerMessage
                .newBuilder()
                .setClientInfo(clientInfo)
                .setType(ServerMessage.Type.SYSTEM)
                .setSystemData(
                        ServerMessage.SystemData
                                .newBuilder()
                                .setType(
                                        ServerMessage.SystemData.Type.UNKNOWN_MESSAGE_TYPE)
                                .build()).build();
        outbound.add(response);
    }

    @Override
    public final void queueOutboundMessage(final ServerMessage message) {
        outbound.add(message);
    }

    @Override
    public final void queueInboundMessage(final ClientMessage message) {
        inbound.add(message);
    }

    /**
     * @param message
     *            Send out-bound message
     */
    private void sendOutboundMessage(final ServerMessage message) {
        // TODO: Work out if it's better just to send stuff directly to the
        // network service
        networkService.processMessage(message);
    }
}
