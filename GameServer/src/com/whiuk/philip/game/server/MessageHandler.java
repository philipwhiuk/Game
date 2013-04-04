package com.whiuk.philip.game.server;

import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;

import com.whiuk.philip.game.server.auth.Account;
import com.whiuk.philip.game.server.auth.AuthService;
import com.whiuk.philip.game.server.auth.ClientNotAuthenticatedException;
import com.whiuk.philip.game.server.game.GameService;
import com.whiuk.philip.game.server.network.NetworkService;
import com.whiuk.philip.game.server.security.SecurityMessageType;
import com.whiuk.philip.game.server.security.SecurityService;
import com.whiuk.philip.game.server.system.SystemService;
import com.whiuk.philip.game.shared.Message;
import com.whiuk.philip.game.shared.Message.AccountData;

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
    private GameService chatService;
    
    
    Queue<Message> inbound;
    Queue<Message> outbound;

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
            Message message;
            message = outbound.poll();
            while(message != null) {
                processOutboundMessage(message);
                message = outbound.poll();
            }
            message = inbound.poll();
            if(message != null) {
                processInboundMessage(message);
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
    public void processInboundMessage(Message message) {
        if(message.getType() == Message.Type.SYSTEM) {
            systemService.processMessage(message);
        } else if(message.getType() == Message.Type.AUTH) {
            authService.processMessage(message.getSource(), (AccountData) message.getData());
        } else {
            Account account = authService.getAccount(message);
            if(account == null) {
                secService.processMessage(SecurityMessageType.CLIENT_NOT_AUTHENTICATED,
                        message.getSource());
            } else {
                switch(message.getType()) {
                    case GAME:
                        gameService.processMessage(account, message.getData());
                        break;
                    case CHAT:
                        chatService.processMessage(authService.getAccount(message),message.getData());
                        break;
                    default:
                        //TODO: Protobuf message builder
                        /*
                        queueMessage(MessageBuilder.buildMessage()
                                .setType(Event.Type.SYSTEM)
                                .setData(DataBuilder.buildData()
                                        .setType(SystemData.Type.UNKNOWN_TYPE));
                                        */
                        break;
                }
            }
        }
    }
    /**
     * @param message
     */
    public void processOutboundMessage(Message message) {
        networkService.processMessage(message);
    }
}
