package com.whiuk.philip.mmorpg.server.auth;

import com.whiuk.philip.mmorpg.server.hibernate.GenericDAO;
import com.whiuk.philip.mmorpg.serverShared.Account;

/**
 * Account data access object interface.
 * @author Philip Whitehouse
 */
public interface AccountDAO extends GenericDAO<Account, Long> {
    /**
     * @param id ID
     * @return Account
     */
    Account findByID(Long id);

    /**
     * @param username Username
     * @return Account
     */
    Account findByUsername(String username);
}
