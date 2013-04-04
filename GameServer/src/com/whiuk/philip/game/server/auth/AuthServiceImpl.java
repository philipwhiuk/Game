package com.whiuk.philip.game.server.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.whiuk.philip.game.server.data.DataService;
import com.whiuk.philip.game.server.network.Connection;
import com.whiuk.philip.game.server.system.SystemService;
import com.whiuk.philip.game.shared.Message;
import com.whiuk.philip.game.shared.Message.AccountData;
import com.whiuk.philip.game.shared.Message.AccountData.LoginAttempt;
import com.whiuk.philip.game.shared.Message.Data;
import com.whiuk.philip.game.shared.Source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Philip
 *
 */
@Service
public class AuthServiceImpl implements AuthService{
    private static final String MULTIPLE_LOGINS_ATTEMPT_MESSAGE =
            "Attempting to login from a connection with an existing login, logging out the other client";
    private static final String BAD_CONNECTION_LOGIN_MESSAGE =
            "Attempting to login from a connection that doesn't exist";
    private static final String  BAD_CONNECTION_LOGOUT_MESSAGE =
            "Attempting to logout a connection that doesn't exist";
    @Autowired
    private SystemService systemService;
    @Autowired
    private DataService<Source,Connection> dataService;
    /**
     *
     */
    private Map<Account,Connection> accounts;
    private Map<Connection,Account> connections;
    private static transient final Logger logger = Logger.getLogger(getClass());

    /**
     *
     */
    public AuthServiceImpl() {
        accounts = new HashMap<Account,Connection>();
        connections = new HashMap<Connection,Account>();
    }

    @Override
    public Account getAccount(Message event) {
        Connection con = dataService.get(event.getSource());
        return connections.get(con);
    }

    @Override
    public Account getAccount(Connection con) {
        return connections.get(con);
    }

    @Override
    public Connection getConnection(Account acc) {
        return accounts.get(acc);
    }

    @Override
    public void processMessage(Source src, AccountData data) {
        Connection con;
        switch(data.getType()) {
            case LOGIN:
                //Handle trying to login twice from the same source.
                con = dataService.get(src);
                if (dataService.get(src) == null) {
                    logger.log(Level.INFO,BAD_CONNECTION_LOGIN_MESSAGE);
                    systemService.processLostConnection(src);
                } else {
                    if (connections.containsKey(con)) {
                        logger.log(Level.INFO, MULTIPLE_LOGINS_ATTEMPT_MESSAGE);
                        accounts.remove(connections.remove(con));
                    }
                    processLoginAttempt(src, data.getLoginAttempt());
                }
                break;
            case LOGOUT:
                con = dataService.get(src);
                if(dataService.get(src) == null) {
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

    private void processLoginAttempt(Source src, LoginAttempt loginAttempt) {
        // TODO Auto-generated method stub
        
    }

}
