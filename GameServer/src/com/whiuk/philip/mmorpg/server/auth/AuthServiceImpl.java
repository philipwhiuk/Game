package com.whiuk.philip.mmorpg.server.auth;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.protobuf.ByteString;
import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage.AuthData;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.server.MessageHandlerService;
import com.whiuk.philip.mmorpg.server.chat.ChatService;
import com.whiuk.philip.mmorpg.server.game.GameService;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;
import com.whiuk.philip.mmorpg.server.system.SystemService;
import com.whiuk.philip.mmorpg.serverShared.Account;
import com.whiuk.philip.mmorpg.serverShared.Connection;
import com.whiuk.philip.mmorpg.serverShared.LoginAttempt;

/**
 * @author Philip
 */
@Service
public class AuthServiceImpl implements AuthService {
    /**
     * Message for multiple logins detected.
     */
    private static final String MULTIPLE_LOGINS_ATTEMPT_MESSAGE = "Attempting to login from a connection with an existing login"
            + ", logging out the other client";
    /**
     * Error message when invalid login occurs.
     */
    private static final String BAD_CONNECTION_LOGIN_MESSAGE = "Attempting to login from a connection that doesn't exist";
    /**
     * Error message when invalid logout occurs.
     */
    private static final String BAD_CONNECTION_LOGOUT_MESSAGE = "Attempting to logout a connection that doesn't exist";
    /**
     * Error message when invalid / unknown authentication message type is received.
     */
    private static final String INVALID_AUTHENTICATION_MESSAGE_TYPE = "Received unknown / unsupported authentication message type";
    /**
     * Class logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(AuthServiceImpl.class);

    /**
     *
     */
    @Autowired
    private SystemService systemService;
    /**
     *
     */
    private Map<Account, Connection> accounts;
    /**
     *
     */
    private Map<Connection, Account> connections;
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
    public AuthServiceImpl() {
        accounts = new HashMap<Account, Connection>();
        connections = new HashMap<Connection, Account>();
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
    public final void processMessage(final ClientInfo src, final AuthData data) {
        Connection con;
        LOGGER.info("Processing authentication message");
        switch (data.getType()) {
            case LOGIN:
                // Handle trying to login twice from the same source.
                con = systemService.getConnection(src);
                if (systemService.getConnection(src) == null) {
                    logger.log(Level.INFO, BAD_CONNECTION_LOGIN_MESSAGE);
                    systemService.processLostConnection(src);
                } else {
                    if (connections.containsKey(con)) {
                        logger.log(Level.INFO, MULTIPLE_LOGINS_ATTEMPT_MESSAGE);
                        accounts.remove(connections.remove(con));
                    }
                    processLoginAttempt(systemService.getConnection(src),
                            data.getUsername(), data.getPassword());
                }
                break;
            case LOGOUT:
                con = systemService.getConnection(src);
                if (systemService.getConnection(src) == null) {
                    logger.log(Level.INFO, BAD_CONNECTION_LOGOUT_MESSAGE);
                } else {
                    /*
                     * Remove the c->a and a->c mapping
                     */
                    accounts.remove(connections.remove(con));
                }
                break;
            default:
                logger.log(Level.INFO, INVALID_AUTHENTICATION_MESSAGE_TYPE);
                throw new UnsupportedOperationException();
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
            final String username, final ByteString byteString) {
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
            if (!account.getPassword().equals(byteString.toStringUtf8())) {
                processFailedLogin(con, attempt, account);
            } else {
                processSuccesfulLogin(con, attempt, account);
            }
        } else {
            processFailedLogin(con, attempt);
        }
    }

    /**
     * Handles a login attempt which didn't match an account.
     * 
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
    }

    /**
     * Handle a failed login against an account.
     * 
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
            Account account = connections.remove(con);
            accounts.remove(account);
            chatService.notifyLogout(account);
            gameService.notifyLogout(account);
        }
    }

}
