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
	 *
	 * @param account
	 * @param chatData
	 */
	void processMessage(Account account, ChatData chatData);

}
