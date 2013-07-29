package com.whiuk.philip.mmorpg.server.auth.service;

import com.whiuk.philip.mmorpg.server.auth.AuthEventListener;
import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.serverShared.Connection;
import com.whiuk.philip.mmorpg.serverShared.LoginAttempt;
import com.whiuk.philip.mmorpg.serverShared.RegistrationAttempt;
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
    /**
     * Handle a failed login request.
     * @param con
     * @param attempt
     * @param account
     */
    void handleFailedLogin(Connection con, LoginAttempt attempt, Account account);
    /**
     * Handle a succesful login request
     * @param con
     * @param attempt
     * @param account
     */
    void handleSuccesfulLogin(Connection con, LoginAttempt attempt,
            Account account);
    /**
     * Check whether the an account with the given username is logged in.
     * @param username Username
     * @return <code>true</code> if logged in.
     */
    boolean hasLoggedInAccount(String username);
    /**
     * 
     * @param con
     * @param attempt
     */
    void handleFailedLogin(Connection con, LoginAttempt attempt);
    /**
     * 
     * @param con
     * @param attempt
     */
    void handleFailedRegistration(Connection con, RegistrationAttempt attempt);
    /**
     * 
     * @param con
     * @param attempt
     * @param account
     */
    void handleSuccesfulRegistration(Connection con,
            RegistrationAttempt attempt, Account account);
}
