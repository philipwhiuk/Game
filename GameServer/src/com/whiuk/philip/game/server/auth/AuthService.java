package com.whiuk.philip.game.server.auth;

import com.whiuk.philip.game.server.network.Connection;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ClientMessage.AccountData;

/**
 * @author Philip
 *
 */
public interface AuthService {

    /**
     * @param event Event from client
     * @return Account connected to the client
     */
    Account getAccount(ClientMessage event);

    /**
     * @param src ClientInfo
     * @param data AccountData for a message
     */
    void processMessage(ClientInfo src, AccountData data);

    /**
     * @param con
     * @return acc
     */
    Account getAccount(Connection con);

    /**
     *
     * @param acc
     * @return conn
     */
    Connection getConnection(Account acc);


}
