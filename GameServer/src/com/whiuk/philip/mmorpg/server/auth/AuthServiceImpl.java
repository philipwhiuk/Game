package com.whiuk.philip.mmorpg.server.auth;

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
import com.whiuk.philip.mmorpg.server.chat.ChatService;
import com.whiuk.philip.mmorpg.server.email.EmailService;
import com.whiuk.philip.mmorpg.server.email.InvalidEmailException;
import com.whiuk.philip.mmorpg.server.game.service.GameService;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;
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
     * Error message when invalid logout occurs.
     */
    private static final String BAD_CONNECTION_LOGOUT_MESSAGE =
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
     * Error message when login received for account already logged in.
     */
    private static final String ALREADY_LOGGED_IN_MESSAGE =
            "Attempting to login (using valid credentials to an account that's"
            + " already logged in. Ignoring request.";
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
     *
     */
    @Autowired
    private AccountDAO accountDAO;
    /**
    *
    */
    @Autowired
    private LoginAttemptDAO loginAttemptDAO;
    /**
     *
     */
    @Autowired
    private RegistrationAttemptDAO registrationAttemptDAO;
    /**
     *
     */
    private EmailService emailService;
    /**
     * Authentication event listeners
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
                } else {
                    // Remove the c->a and a->c mapping
                    accounts.remove(connections.remove(con));
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
     *
     * @param src
     * @param data
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
                accounts.remove(connections.remove(con));
            }
            processRegistrationAttempt(
                    systemService.getConnection(src),
                    data.getUsername(),
                    data.getPassword().toStringUtf8(),
                    data.getEmail());
        }
    }

    /**
     *
     * @param src
     * @param data
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
                processLoginAttempt(systemService.getConnection(src),
                        data.getUsername(),
                        data.getPassword().toStringUtf8());
            }
        }
    }

    /**
     * @param con
     *            Connection
     * @param username
     *            Username
     * @param byteString
     *            Password
     */
    private void processLoginAttempt(final Connection con,
            final String username, final String password) {
        LOGGER.trace("Processing login attempt");
        // TODO Exceeded maximum login attempts
        HibernateUtils.beginTransaction();
        Account account = accountDAO.findByUsername(username);
        HibernateUtils.commitTransaction();
        HibernateUtils.beginTransaction();
        LoginAttempt attempt = new LoginAttempt();
        attempt.setTime(System.currentTimeMillis());
        attempt.setAccount(account);
        attempt.setConnection(con.toString());
        System.out.println(attempt.getTime());
        loginAttemptDAO.save(attempt);
        HibernateUtils.commitTransaction();
        if (account != null) {
            if (!account.getPassword().equals(password)) {
                processFailedLogin(con, attempt, account);
            } else {
                //Handle account already logged in
                Set<Account> currentAccounts = accounts.keySet();
                for (Account a: currentAccounts) {
                    if (a.getUsername().equals(username)) {
                        logger.log(Level.INFO, ALREADY_LOGGED_IN_MESSAGE);
                        processFailedLogin(con, attempt, account);
                        return;
                    }
                }
                processSuccesfulLogin(con, attempt, account);
            }
        } else {
            processFailedLogin(con, attempt);
        }
    }

    /**
     * @param con
     * @param username
     * @param password
     * @param email
     */
    private void processRegistrationAttempt(final Connection con,
            final String username, final String password, final String email) {
        // TODO Exceeded maximum registration attempts
        HibernateUtils.beginTransaction();
        Account account = accountDAO.findByUsername(username);
        HibernateUtils.commitTransaction();
        HibernateUtils.beginTransaction();
        RegistrationAttempt attempt = new RegistrationAttempt();
        attempt.setTime(System.currentTimeMillis());
        attempt.setAccount(account);
        attempt.setEmail(email);
        attempt.setConnection(con.toString());
        System.out.println(attempt.getTime());
        registrationAttemptDAO.save(attempt);
        HibernateUtils.commitTransaction();
        if (account != null) {
            processFailedRegistration(con, attempt);
        } else {
            HibernateUtils.beginTransaction();
            account = new Account();
            account.setUsername(username);
            account.setPassword(password);
            account.setEmail(email);
            accountDAO.save(account);
            HibernateUtils.commitTransaction();
            processSuccesfulRegistration(con, attempt, account);
        }
    }

    /**
     * Handles a successful registration.
     * @param con Connection
     * @param attempt Attempt
     * @param account Account
     */
    private void processSuccesfulRegistration(final Connection con,
            final RegistrationAttempt attempt, final Account account) {
        // TODO Any other post-registration steps?
        try {
            emailService.sendRegistrationEmail(account);
        } catch (InvalidEmailException e) {
            HibernateUtils.beginTransaction();
            account.setEmailInvalid(true);
            accountDAO.save(account);
            HibernateUtils.commitTransaction();
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
    private void processFailedRegistration(final Connection con,
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
    private void processFailedLogin(final Connection con,
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
    private void processSuccesfulLogin(final Connection con,
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
        for(AuthEventListener l : authEventListeners) {
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
    private void processFailedLogin(final Connection con,
            final LoginAttempt attempt, final Account account) {
        account.addLoginAttempt(attempt);
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
     * @param account
     */
    private void performLogout(final Account account) {
        connections.remove(accounts.remove(account));
        for (AuthEventListener l : authEventListeners) {
            l.notifyLogout(account);
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

}
