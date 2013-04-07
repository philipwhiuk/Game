package com.whiuk.philip.game.server.chat;

import org.springframework.stereotype.Service;

import com.whiuk.philip.game.server.auth.Account;
import com.whiuk.philip.game.shared.Messages.ClientMessage.ChatData;

/**
 * @author Philip Whitehouse
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Override
    public void processMessage(final Account account, final ChatData chatData) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyLogout(final Account account) {
        // TODO Auto-generated method stub
    }

}
