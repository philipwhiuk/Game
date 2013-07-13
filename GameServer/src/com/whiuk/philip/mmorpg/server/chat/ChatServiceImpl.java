package com.whiuk.philip.mmorpg.server.chat;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.server.auth.AuthService;
import com.whiuk.philip.mmorpg.serverShared.Account;
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
     * <p>Create a new chat service.</p>
     * <ul>
     * <li>Creates a chat service to serve channels.</li>
     * <li>Creates the global public channel (ID:0).</li>
     * </ul>
     */
    public ChatServiceImpl() {
        channels = new HashMap<Integer, ChatChannel>();
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
                c.sendMessage(account, chatData.getMessage());
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
    public void notifyLogin(Account account) {
        // TODO Auto-generated method stub
        
    }

}
