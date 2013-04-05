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
     * Get an account associated with a client message.
     * @param event Event from client
     * @return Account connected to the client
     */
    Account getAccount(ClientMessage event);

    /**
     * Process client account message.
     * @param src ClientInfo
     * @param data AccountData for a message
     */
    void processMessage(ClientInfo src, AccountData data);

    /**
     * Get account by connection.
     * @param con Connection
     * @return Account
     */
    Account getAccount(Connection con);

    /**
     * Get connection by account.
     * @param acc Account
     * @return Connection
     */
    Connection getConnection(Account acc);


}
