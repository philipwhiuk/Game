package com.whiuk.philip.game.server.chat;

import com.whiuk.philip.game.server.auth.Account;
import com.whiuk.philip.game.shared.Messages.ClientMessage.ChatData;

/**
 *
 * @author Philip Whitehouse
 *
 */
public interface ChatService {
    /**
	 * Process chat message from account.
	 * @param account Account
	 * @param chatData Chat message
	 */
	void processMessage(Account account, ChatData chatData);

}
