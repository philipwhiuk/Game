package com.whiuk.philip.game.server.auth;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.hibernate.criterion.Restrictions;

import com.whiuk.philip.game.server.HibernateUtils;
import com.whiuk.philip.game.server.MessageHandlerService;
import com.whiuk.philip.game.server.chat.ChatService;
import com.whiuk.philip.game.server.game.GameService;
import com.whiuk.philip.game.server.system.Connection;
import com.whiuk.philip.game.server.system.SystemService;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ServerMessage;
import com.whiuk.philip.game.shared.Messages.ClientMessage.AuthData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Philip
 *
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
    private static final String  BAD_CONNECTION_LOGOUT_MESSAGE =
            "Attempting to logout a connection that doesn't exist";
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
    public final void processMessage(final ClientInfo src,
    		final AuthData data) {
        Connection con;
        switch(data.getType()) {
            case LOGIN:
                //Handle trying to login twice from the same source.
                con = systemService.getConnection(src);
                if (systemService.getConnection(src) == null) {
                    logger.log(Level.INFO, BAD_CONNECTION_LOGIN_MESSAGE);
                    systemService.processLostConnection(src);
                } else {
                    if (connections.containsKey(con)) {
                        logger.log(Level.INFO, MULTIPLE_LOGINS_ATTEMPT_MESSAGE);
                        accounts.remove(connections.remove(con));
                    }
                    processLoginAttempt(systemService.getConnection(src), data.getUsername(),
                    		data.getPassword());
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
                break;
        }
    }
    /**
     *
     * @param src Client
     * @param username Username
     * @param password Password
     */
    private void processLoginAttempt(final Connection con,
    		final String username, final String password) {
    	//TODO Exceeded maximum login attempts
        Account account = (Account) HibernateUtils.getSessionFactory().openSession()
        	.createCriteria(Account.class)
        	.add(Restrictions.eq("username", username)).uniqueResult();
        if (account != null) {
        	if (!account.getPassword().equals(password)) {
        		processFailedLogin(con, account);
        	} else {
        		processSuccesfulLogin(con, account);
        	}
        } else {
        	processFailedLogin(con);
        }
    }

    /**
     * Handles a login attempt which didn't match an account.
     * @param con Connection
     */
	private void processFailedLogin(final Connection con) {
		con.setLastLoginAttempt(System.nanoTime());
		ServerMessage message = ServerMessage
				.newBuilder()
				.setType(ServerMessage.Type.AUTH)
				.setAuthData(ServerMessage.AuthData.newBuilder()
						.setType(ServerMessage
						.AuthData.Type.LOGIN_FAILED)
						.build())
				.build();
		messageHandler.queueOutboundMessage(message);
	}
	/**
	 * Handle a successful login attempt.
	 * @param con Connection
	 * @param account Account
	 */
	private void processSuccesfulLogin(final Connection con,
			final Account account) {
		//TODO: Further authentication checks
		connections.put(con, account);
		accounts.put(account, con);
		ServerMessage message = ServerMessage
				.newBuilder()
				.setType(ServerMessage.Type.AUTH)
				.setAuthData(ServerMessage.AuthData.newBuilder()
					.setType(ServerMessage
					.AuthData.Type.LOGIN_SUCCESSFUL)
					.setUsername(account.getUsername())
					.build())
				.build();
		messageHandler.queueOutboundMessage(message);
	}
	/**
	 * Handle a failed login against an account.
	 * @param con Connection
	 * @param account Account
	 */
	private void processFailedLogin(final Connection con,
			final Account account) {
		account.setLastLoginAttempt(System.nanoTime());
		con.setLastLoginAttempt(System.nanoTime());
		ServerMessage message = ServerMessage
				.newBuilder()
				.setType(ServerMessage.Type.AUTH)
				.setAuthData(ServerMessage.AuthData.newBuilder()
						.setType(ServerMessage
						.AuthData.Type.LOGIN_FAILED)
						.build())
				.build();
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
