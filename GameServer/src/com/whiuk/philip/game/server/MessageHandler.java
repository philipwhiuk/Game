package com.whiuk.philip.game.server;

import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;

import com.whiuk.philip.game.server.auth.Account;
import com.whiuk.philip.game.server.auth.AuthService;
import com.whiuk.philip.game.server.auth.ClientNotAuthenticatedException;
import com.whiuk.philip.game.server.chat.ChatService;
import com.whiuk.philip.game.server.game.GameService;
import com.whiuk.philip.game.server.network.NetworkService;
import com.whiuk.philip.game.server.security.SecurityMessageType;
import com.whiuk.philip.game.server.security.SecurityService;
import com.whiuk.philip.game.server.system.SystemService;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ServerMessage;
import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 * @author Philip
 *
 */
public class MessageHandler implements Runnable {
    @Autowired
    private NetworkService networkService;
    @Autowired
    private AuthService authService;
    @Autowired
    private GameService gameService;
    @Autowired
    private SecurityService secService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ChatService chatService;
    
    
    Queue<ClientMessage> inbound;
    Queue<ServerMessage> outbound;

    private boolean running;
    /**
     *
     */
    public MessageHandler() {

    }

    @Override
    public void run() {
        running = true;
        while(running) {
            boolean processed = false;
            ServerMessage serverMessage;
            serverMessage = outbound.poll();
            while(serverMessage != null) {
                processOutboundMessage(serverMessage);
                serverMessage = outbound.poll();
            }
            ClientMessage clientMessage = inbound.poll();
            if(clientMessage != null) {
                processInboundMessage(clientMessage);
            }
            if(!processed) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){

                }
            }
        }
     }

    /**
     * @param message
     */
    public final void processInboundMessage(ClientMessage message) {
    	if(message.hasSystemData()) {
            systemService.processMessage(message.getSystemData());
        } else if(message.hasAccountData()) {
            authService.processMessage(message.getClientInfo(),
            		message.getAccountData());
        } else {
            Account account = authService.getAccount(message);
            if (account == null) {
                secService.processMessage(
                		SecurityMessageType.CLIENT_NOT_AUTHENTICATED,
                        message.getClientInfo());
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
    /**
     * Handles messages of an unknown type.
     */
    private void handleUnknownMessageType(final ClientInfo clientInfo) {
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

	/**
     * @param message
     */
    public final void processOutboundMessage(final ServerMessage message) {
    	//TODO: Work out if it's better just to send stuff directly to the network service
        networkService.processMessage(message);
    }
}
