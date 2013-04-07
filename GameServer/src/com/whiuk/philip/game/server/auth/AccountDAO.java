package com.whiuk.philip.game.server.auth;

import com.whiuk.philip.game.server.hibernate.GenericDAO;

/**
 * Account data access object interface.
 * 
 * @author Philip Whitehouse
 */
public interface AccountDAO extends GenericDAO<Account, Long> {
    /**
     * @param id
     * @return
     */
    Account findByID(Long id);

    /**
     * @param username
     * @return
     */
    Account findByUsername(String username);
}
