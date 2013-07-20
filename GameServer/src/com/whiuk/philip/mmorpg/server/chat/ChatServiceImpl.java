package com.whiuk.philip.mmorpg.server.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.server.MessageHandlerService;
import com.whiuk.philip.mmorpg.server.auth.AuthService;
import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.ChatData;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage.ChatData.Type;

/**
 * @author Philip Whitehouse
 */
@Service
public class ChatServiceImpl implements ChatService {
    /**
    *
    */
   private final transient Logger logger = Logger.getLogger(getClass());

    /**
     * Mapping of channels by ID.
     */
    private Map<Integer, ChatChannel> channels;
    /**
     * 
     */
    @Autowired
    private AuthService authService;
    /**
    *
    */
    @Autowired
    private MessageHandlerService messageHandler;

    /**
     * <p>Create a new chat service.</p>
     * <ul>
     * <li>Creates a chat service to serve channels.</li>
     * <li>Creates the global public channel (ID:0).</li>
     * </ul>
     */
    public ChatServiceImpl() {
        channels = new HashMap<Integer, ChatChannel>();
        //TODO: Consider database storage of channel data
        //Create server public channel
        ChatChannel publicChatChannel = new ChatChannel();
        publicChatChannel.setChatService(this);
        channels.put(0, publicChatChannel);
    }

    @PostConstruct
    public final void init() {
        authService.registerAuthEventListener(this);
    }

    @Override
    public final void processMessage(
            final Account account, final ChatData chatData) {
        logger.trace("Processing chat message");
        if (chatData.getPrivate()) {
            if (chatData.hasTarget()) {
                handlePrivateMessage(account, chatData.getMessage(),
                    chatData.getTarget());
            } else {
                //Handle invalid message
            }
        } else {
            ChatChannel c = channels.get(chatData.getChannel());
            logger.trace("Processing chat message for channel: " + c.getId());
            if (c != null
                    && c.hasAccountRegistered(account)
                    && c.hasAccountSendPrivilege(account)) {
                logger.trace("Processing allowed chat message for channel: " + c.getId());
                c.processMessage(account, chatData.getMessage());
            } else {
                //Handle unauthorised message.
            }
        }
    }

    /**
     * Handle the sending of a private message
     * from <code>src</code> to <code>target</code>.
     * @param src Source of message
     * @param message Message contents
     * @param target Target
     */
    private void handlePrivateMessage(final Account src, final String message,
            final String target) {
        // TODO Auto-generated method stub
    }

    @Override
    public void notifyLogout(final Account account) {
        // TODO Auto-generated method stub
    }

    @Override
    public void notifyLogin(final Account account) {
        //TODO: Move this to account creation
        //Register un-registered accounts to the public chat channel
        if (!channels.get(0).hasAccountRegistered(account)) {
            channels.get(0).registerAccount(account);
        }
        //TODO: User channel join settings
        for (Entry<Integer, ChatChannel> e : channels.entrySet()) {
            if (e.getValue().hasAccountRegistered(account)) {
                e.getValue().join(account);
            }
        }
    }

    @Override
    public final void sendMessageFromChannel(final int id,
            final Account src, final Account target,
            final String messageText) {
        logger.trace("Sending message from channel");
        ServerMessage message = ServerMessage
                .newBuilder()
                .setType(ServerMessage.Type.CHAT)
                .setClientInfo(authService.getConnection(target)
                        .getClientInfo())
                .setChatData(
                        ServerMessage.ChatData
                        .newBuilder()
                        .setType(Type.MESSAGE)
                        .setPrivate(false)
                        .setSource(src.getUsername())
                        .setChannel(id)
                        .setMessage(messageText)
                        .build())
                .build();
        messageHandler.queueOutboundMessage(message);
    }

    @Override
    public void sendPlayerJoinedChannel(
            final int id, final Account src, final Account target) {
        logger.trace("Sending joined channel");
        ServerMessage message = ServerMessage
                .newBuilder()
                .setType(ServerMessage.Type.CHAT)
                .setClientInfo(authService.getConnection(target)
                        .getClientInfo())
                .setChatData(
                        ServerMessage.ChatData
                        .newBuilder()
                        .setType(Type.PLAYER_JOINED)
                        .setPrivate(false)
                        .setSource(src.getUsername())
                        .setChannel(id)
                        .build())
                .build();
        messageHandler.queueOutboundMessage(message);
    }

}
