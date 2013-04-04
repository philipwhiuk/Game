package com.whiuk.philip.game.server.auth;

import com.whiuk.philip.game.server.network.Connection;
import com.whiuk.philip.game.shared.Message;
import com.whiuk.philip.game.shared.Message.AccountData;
import com.whiuk.philip.game.shared.Message.Data;
import com.whiuk.philip.game.shared.Source;

/**
 * @author Philip
 *
 */
public interface AuthService {

    /**
     * @param event Event from client
     * @return Account connected to the client
     */
    Account getAccount(Message event);

    /**
     * @param src Source
     * @param data AccountData for a message
     */
    void processMessage(Source src, AccountData data);

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
