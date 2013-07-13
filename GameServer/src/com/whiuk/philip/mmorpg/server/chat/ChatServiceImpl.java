package com.whiuk.philip.mmorpg.server.chat;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.server.MessageHandlerService;
import com.whiuk.philip.mmorpg.server.auth.AuthService;
import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.ChatData;

/**
 * @author Philip Whitehouse
 */
@Service
public class ChatServiceImpl implements ChatService {

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
        channels.put(0, new ChatChannel());
    }

    @PostConstruct
    public final void init() {
        authService.registerAuthEventListener(this);
    }

    @Override
    public final void processMessage(
            final Account account, final ChatData chatData) {
        if (chatData.getPrivate()) {
            if (chatData.hasTarget()) {
                handlePrivateMessage(account, chatData.getMessage(),
                    chatData.getTarget());
            } else {
                //Handle invalid message
            }
        } else {
            ChatChannel c = channels.get(chatData.getChannel());
            if (c != null
                    && c.hasAccountRegistered(account)
                    && c.hasAccountSendPrivilege(account)) {
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
    }

    @Override
    public final void sendMessageFromChannel(final int id,
            final Account src, final Account target,
            final String messageText) {
        ServerMessage message = ServerMessage
                .newBuilder()
                .setType(ServerMessage.Type.AUTH)
                .setClientInfo(authService.getConnection(target)
                        .getClientInfo())
                .setChatData(
                        ServerMessage.ChatData
                        .newBuilder()
                        .setSource(target.getUsername())
                        .setChannel(id)
                        .setMessage(messageText)
                        .build())
                .build();
        messageHandler.queueOutboundMessage(message);
    }

}
