package com.whiuk.philip.game.server.chat;

import com.whiuk.philip.game.server.auth.Account;
import com.whiuk.philip.game.shared.Messages.ClientMessage.ChatData;

public interface ChatService {

	void processMessage(Account account, ChatData chatData);

}
