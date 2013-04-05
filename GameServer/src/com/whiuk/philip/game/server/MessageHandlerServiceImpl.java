package com.whiuk.philip.game.server;

import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.game.server.auth.Account;
import com.whiuk.philip.game.server.auth.AuthService;
import com.whiuk.philip.game.server.chat.ChatService;
import com.whiuk.philip.game.server.game.GameService;
import com.whiuk.philip.game.server.network.Connection;
import com.whiuk.philip.game.server.network.NetworkService;
import com.whiuk.philip.game.server.security.SecurityService;
import com.whiuk.philip.game.server.system.SystemService;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * @author Philip
 *
 */
@Service
public class MessageHandlerServiceImpl
    implements Runnable, MessageHandlerService {
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
     * Whether the message handler is running.
     */
    private boolean running;
    /**
     *
     */
    public MessageHandlerServiceImpl() {

    }

    @Override
	public final void run() {
        running = true;
        while (running) {
            boolean processed = false;
            ServerMessage serverMessage;
            serverMessage = outbound.poll();
            while (serverMessage != null) {
                processOutboundMessage(serverMessage);
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

    @Override
    public final void processInboundMessage(final ClientMessage message) {
    	if (message.hasSystemData()) {
            systemService.processMessage(message.getSystemData());
        } else if (message.hasAccountData()) {
            authService.processMessage(message.getClientInfo(),
            		message.getAccountData());
        } else {
            Account account = authService.getAccount(message);
            if (account == null) {
                secService.handleMessageFromUnauthenticatedClient(message);
            } else {
                switch(message.getType()) {
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
	public final void handleUnknownMessageType(
			final Connection connection) {
		// TODO Auto-generated method stub
		handleUnknownMessageType(
				networkService.getClientInfo(connection));
	}

    @Override
    public final void handleUnknownMessageType(final ClientInfo clientInfo) {
    	ServerMessage response = ServerMessage.newBuilder()
    			.setClientInfo(clientInfo)
        		.setType(ServerMessage.Type.SYSTEM)
        		.setSystemData(ServerMessage.SystemData.newBuilder()
        				.setType(
			ServerMessage.SystemData.Type.UNKNOWN_MESSAGE_TYPE)
        				.build())
        		.build();
    	outbound.add(response);
	}

	@Override
    public final void processOutboundMessage(final ServerMessage message) {
    	//TODO: Work out if it's better just to send stuff directly to the network service
        networkService.processMessage(message);
    }
}
