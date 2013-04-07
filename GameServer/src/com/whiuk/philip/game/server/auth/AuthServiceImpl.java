package com.whiuk.philip.game.server.auth;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.whiuk.philip.game.server.chat.ChatService;
import com.whiuk.philip.game.server.data.DataService;
import com.whiuk.philip.game.server.game.GameService;
import com.whiuk.philip.game.server.system.Connection;
import com.whiuk.philip.game.server.system.SystemService;
import com.whiuk.philip.game.shared.Messages.ClientInfo;
import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ClientMessage.AccountData;

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
    		final AccountData data) {
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
                    processLoginAttempt(src, data.getUsername(),
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
    private void processLoginAttempt(final ClientInfo src,
    		final String username, final String password) {
        // TODO Auto-generated method stub

    }

	@Override
	public void notifyDisconnection(Connection con) {
		if (connections.containsKey(con)) {
			Account account = connections.remove(con);
			accounts.remove(account);
			chatService.notifyLogout(account);
			gameService.notifyLogout(account);
		}
	}

}
