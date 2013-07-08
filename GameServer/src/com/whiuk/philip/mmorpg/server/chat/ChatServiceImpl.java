package com.whiuk.philip.mmorpg.server.chat;

import java.util.Map;

import org.springframework.stereotype.Service;

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

}
