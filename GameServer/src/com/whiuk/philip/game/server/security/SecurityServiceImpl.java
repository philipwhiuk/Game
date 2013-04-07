package com.whiuk.philip.game.server.security;

import org.springframework.stereotype.Service;

import com.whiuk.philip.game.shared.Messages.ClientMessage;

/**
 * @author Philip Whitehouse
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    public void handleMessageFromUnauthenticatedClient(
            final ClientMessage message) {
        // TODO Auto-generated method stub

    }

}
