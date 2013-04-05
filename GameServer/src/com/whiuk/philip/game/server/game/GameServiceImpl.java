package com.whiuk.philip.game.server.game;

import org.springframework.stereotype.Service;

import com.whiuk.philip.game.server.auth.Account;
import com.whiuk.philip.game.shared.Messages.ClientMessage.GameData;

@Service
public class GameServiceImpl implements GameService {

	@Override
    public void processMessage(final Account account, final GameData data) {
        // TODO Auto-generated method stub
    }

}
