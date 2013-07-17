package com.whiuk.philip.mmorpg.server.auth;

import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.serverShared.Connection;
import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.AuthData;

/**
 * @author Philip
 */
public interface AuthService {

    /**
     * Get an account associated with a client message.
     * @param event
     *            Event from client
     * @return Account connected to the client
     */
    Account getAccount(ClientMessage event);

    /**
     * Process client account message.
     * @param src
     *            ClientInfo
     * @param data
     *            AccountData for a message
     */
    void processMessage(ClientInfo src, AuthData data);

    /**
     * Get account by client info.
     * @param clientInfo
     *            ClientInfo
     * @return Account
     */
    Account getAccount(ClientInfo clientInfo);

    /**
     * Get connection by account.
     * @param acc
     *            Account
     * @return ClientInfo
     */
    Connection getConnection(Account acc);

    /**
     * Notify the service of a client disconnection.
     * @param connection connection
     */
    void notifyDisconnection(Connection connection);
    /**
     * @param listener Authentication event listener to add
     */
    void registerAuthEventListener(AuthEventListener listener);
    /**
     * @param listener Authentication event listener to remove
     */
    void deregisterAuthEventListener(AuthEventListener listener);

}
