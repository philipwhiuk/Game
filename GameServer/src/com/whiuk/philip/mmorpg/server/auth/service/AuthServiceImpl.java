package com.whiuk.philip.mmorpg.server.auth.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.AuthData;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.server.MessageHandlerService;
import com.whiuk.philip.mmorpg.server.auth.AuthEventListener;
import com.whiuk.philip.mmorpg.server.auth.controller.AccountEmailController;
import com.whiuk.philip.mmorpg.server.auth.controller.LoginController;
import com.whiuk.philip.mmorpg.server.auth.controller.RegistrationController;
import com.whiuk.philip.mmorpg.server.chat.ChatService;
import com.whiuk.philip.mmorpg.server.email.EmailService;
import com.whiuk.philip.mmorpg.server.email.InvalidEmailException;
import com.whiuk.philip.mmorpg.server.game.service.GameService;
import com.whiuk.philip.mmorpg.server.system.SystemService;
import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.serverShared.Connection;
import com.whiuk.philip.mmorpg.serverShared.LoginAttempt;
import com.whiuk.philip.mmorpg.serverShared.RegistrationAttempt;

/**
 * @author Philip
 */
@Service
public class AuthServiceImpl implements AuthService {
    /**
     * Message for multiple logins detected.
     */
    private static final String MULTIPLE_LOGINS_ATTEMPT_MESSAGE =
            "Attempting to login from a connection with an existing login"
            + ", logging out the other client";
    /**
     * Error message when invalid login occurs.
     */
    private static final String BAD_CONNECTION_LOGIN_MESSAGE =
            "Attempting to login from a connection that doesn't exist";
    /**
     * Error message when logout from unknown connection occurs.
     */
    private static final String BAD_CONNECTION_LOGOUT_MESSAGE =
            "Attempting to logout a connection that doesn't exist";
    /**
     * Error message when trying to logout without an existing login.
     */
    private static final String LOGOUT_WITHOUT_LOGIN_MESSAGE =
            "Attempting to logout a connection that doesn't exist";
    /**
     * Error message when invalid / unknown
     * authentication message type is received.
     */
    private static final String INVALID_AUTHENTICATION_MESSAGE_TYPE =
            "Received unknown / unsupported authentication message type";
    /**
     * Error message when registration received from client that is logged in.
     */
    private static final String REGISTER_FROM_EXISTING_LOGIN_ATTEMPT_MESSAGE =
            "Attempting to register from a connection with an existing login"
            + ", logging out the other client";
    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(AuthServiceImpl.class);
    /**
     * Error message when login received without both username and password
     * (should be impossible on a valid client).
     */
    private static final String MISSING_LOGIN_DATA_MESSAGE =
            "Attempting to login without providing both a username and "
            + "password";
    /**
     *
     */
    @Autowired
    private SystemService systemService;
    /**
     *
     */
    private volatile Map<Account, Connection> accounts;
    /**
     *
     */
    private volatile Map<Connection, Account> connections;
    /**
     *
     */
    private final transient Logger logger = Logger.getLogger(getClass());
    /**
     *
     */
    @Autowired
    private ChatService chatService;
    /**
     *
     */
    @Autowired
    private GameService gameService;
    /**
     *
     */
    @Autowired
    private MessageHandlerService messageHandler;
    /**
     * Login controller.
     */
    @Autowired
    private LoginController loginController;
    /**
     * Registration controller.
     */
    @Autowired
    private RegistrationController registrationController;
    /**
     * Email service.
     */
    @Autowired
    private EmailService emailService;
    /**
     * Account email controller.
     */
    @Autowired
    private AccountEmailController accountEmailController;
    /**
     * Authentication event listeners.
     */
    private ArrayList<AuthEventListener> authEventListeners;

    /**
     *
     */
    public AuthServiceImpl() {
        accounts = new HashMap<Account, Connection>();
        connections = new HashMap<Connection, Account>();
        authEventListeners = new ArrayList<AuthEventListener>();
    }

    @Override
    public final Account getAccount(final ClientMessage message) {
        Connection con = systemService.getConnection(message.getClientInfo());
        return connections.get(con);
    }

    @Override
    public final Account getAccount(final ClientInfo clientInfo) {
        return connections.get(clientInfo);
    }

    @Override
    public final Connection getConnection(final Account acc) {
        return accounts.get(acc);
    }

    @Override
    public final void processMessage(
            final ClientInfo src, final AuthData data) {
        Connection con;
        LOGGER.trace("Processing authentication message");
        switch (data.getType()) {
            case LOGIN:
                processLoginMessage(src, data);
                break;
            case LOGOUT:
                con = systemService.getConnection(src);
                if (systemService.getConnection(src) == null) {
                    logger.log(Level.INFO, BAD_CONNECTION_LOGOUT_MESSAGE);
                } else if (connections.containsKey(con)) {
                    performLogout(connections.get(con));
                } else {
                    LOGGER.log(Level.INFO, LOGOUT_WITHOUT_LOGIN_MESSAGE);
                }
                break;
            case REGISTER:
                processRegisterMessage(src, data);
                break;
            default:
                logger.log(Level.INFO, INVALID_AUTHENTICATION_MESSAGE_TYPE);
                throw new UnsupportedOperationException();
        }
    }

    /**
     * @param src Source client information
     * @param data Authentication data
     */
    private void processRegisterMessage(final ClientInfo src,
            final AuthData data) {
        Connection con;
        con = systemService.getConnection(src);
        if (systemService.getConnection(src) == null) {
            logger.log(Level.INFO, BAD_CONNECTION_LOGIN_MESSAGE);
            systemService.processLostConnection(src);
        } else {
            if (connections.containsKey(con)) {
                logger.log(Level.INFO,
                        REGISTER_FROM_EXISTING_LOGIN_ATTEMPT_MESSAGE);
                performLogout(connections.get(con));
            }
            registrationController.processRegistrationAttempt(
                    systemService.getConnection(src),
                    data.getUsername(),
                    data.getPassword().toStringUtf8(),
                    data.getEmail());
        }
    }

    /**
     * @param src Source
     * @param data Authentication data
     */
    private void processLoginMessage(
            final ClientInfo src, final AuthData data) {
        LOGGER.trace("Processing login message");
        Connection con;

        con = systemService.getConnection(src);
        //Handle logging in from disconnected client.
        if (con == null) {
            logger.log(Level.INFO, BAD_CONNECTION_LOGIN_MESSAGE);
            systemService.processLostConnection(src);
        } else {
            //Handle client already logged in
            if (connections.containsKey(con)) {
                logger.log(Level.INFO, MULTIPLE_LOGINS_ATTEMPT_MESSAGE);
                performLogout(connections.get(con));
            }
            if (!data.hasUsername() || !data.hasPassword()) {
                logger.log(Level.INFO, MISSING_LOGIN_DATA_MESSAGE);
            } else {
                loginController.processLoginAttempt(
                        systemService.getConnection(src),
                        data.getUsername(),
                        data.getPassword().toStringUtf8());
            }
        }
    }

    /**
     * Handles a successful registration.
     * @param con Connection
     * @param attempt Attempt
     * @param account Account
     */
    public final void handleSuccesfulRegistration(final Connection con,
            final RegistrationAttempt attempt, final Account account) {
        // TODO Any other post-registration steps?
        try {
            emailService.sendRegistrationEmail(account);
        } catch (InvalidEmailException e) {
            accountEmailController.invalidateEmail(account);
        }

        accounts.put(account, con);
        ServerMessage message = ServerMessage
                .newBuilder()
                .setType(ServerMessage.Type.AUTH)
                .setClientInfo(con.getClientInfo())
                .setAuthData(
                ServerMessage.AuthData
                        .newBuilder()
                        .setType(
                        ServerMessage.AuthData.Type.REGISTRATION_SUCCESSFUL)
                        .setUsername(account.getUsername()).build())
                .build();
        messageHandler.queueOutboundMessage(message);
    }

    /**
     * Handles a registration attempt which matched an existing account.
     * @param con
     *            Connection
     * @param attempt
     *            RegistrationAttempt
     */
    public final void handleFailedRegistration(final Connection con,
            final RegistrationAttempt attempt) {
        con.addRegistrationAttempt(attempt);
        ServerMessage message = ServerMessage
                .newBuilder()
                .setType(ServerMessage.Type.AUTH)
                .setClientInfo(con.getClientInfo())
                .setAuthData(
                ServerMessage.AuthData
                        .newBuilder()
                        .setType(
                        ServerMessage.AuthData.Type.REGISTRATION_FAILED)
                        .setErrorMessage("Account already exists")
                        .build()).build();
        messageHandler.queueOutboundMessage(message);
    }

    /**
     * Handles a login attempt which didn't match an account.
     * @param con
     *            Connection
     * @param attempt
     *            LoginAttempt
     */
    public final void handleFailedLogin(final Connection con,
            final LoginAttempt attempt) {
        con.addLoginAttempt(attempt);
        ServerMessage message = ServerMessage
                .newBuilder()
                .setType(ServerMessage.Type.AUTH)
                .setClientInfo(con.getClientInfo())
                .setAuthData(
                ServerMessage.AuthData
                        .newBuilder()
                        .setType(
                        ServerMessage.AuthData.Type.LOGIN_FAILED)
                        .setErrorMessage("Invalid username / password")
                        .build()).build();
        messageHandler.queueOutboundMessage(message);
    }

    /**
     * Handle a successful login attempt.
     * @param con
     *            Connection
     * @param account
     *            Account
     * @param attempt
     *            LoginAttempt
     */
    public final void handleSuccesfulLogin(final Connection con,
            final LoginAttempt attempt, final Account account) {
        // TODO: Further authentication checks
        connections.put(con, account);
        accounts.put(account, con);
        ServerMessage message = ServerMessage
                .newBuilder()
                .setType(ServerMessage.Type.AUTH)
                .setClientInfo(con.getClientInfo())
                .setAuthData(
                ServerMessage.AuthData
                        .newBuilder()
                        .setType(
                                ServerMessage.AuthData.Type.LOGIN_SUCCESSFUL)
                        .setUsername(account.getUsername()).build())
                .build();
        messageHandler.queueOutboundMessage(message);
        for (AuthEventListener l : authEventListeners) {
            l.notifyLogin(account);
        }
    }

    /**
     * Handle a failed login against an account.
     * @param con
     *            Connection
     * @param account
     *            Account
     * @param attempt
     *            LoginAttempt
     */
    public final void handleFailedLogin(final Connection con,
            final LoginAttempt attempt, final Account account) {
        ServerMessage message = ServerMessage
                .newBuilder()
                .setType(ServerMessage.Type.AUTH)
                .setClientInfo(con.getClientInfo())
                .setAuthData(
                ServerMessage.AuthData
                        .newBuilder()
                        .setType(
                                ServerMessage.AuthData.Type.LOGIN_FAILED)
                        .build()).build();
        messageHandler.queueOutboundMessage(message);
    }

    @Override
    public final void notifyDisconnection(final Connection con) {
        if (connections.containsKey(con)) {
            performLogout(connections.get(con));
        }
    }

    /**
     * @param a account
     */
    private void performLogout(final Account a) {
        LOGGER.trace("Logging out account");
        // Remove the c->a and a->c mapping
        connections.remove(accounts.remove(a));
        for (AuthEventListener l : authEventListeners) {
            l.notifyLogout(a);
        }
    }

    @Override
    public final void registerAuthEventListener(
            final AuthEventListener listener) {
        authEventListeners.add(listener);
    }

    @Override
    public final void deregisterAuthEventListener(
            final AuthEventListener listener) {
        authEventListeners.remove(listener);
    }

    //TODO: Improve efficient [O(n) search]
    @Override
    public boolean hasLoggedInAccount(final String username) {
        Set<Account> currentAccounts = accounts.keySet();
        for (Account a: currentAccounts) {
            if (a.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

}
