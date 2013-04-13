package com.whiuk.philip.game.server.auth;

import com.whiuk.philip.game.server.hibernate.GenericDAO;
import com.whiuk.philip.game.serverShared.LoginAttempt;

/**
 * Account data access object interface.
 * 
 * @author Philip Whitehouse
 */
public interface LoginAttemptDAO extends GenericDAO<LoginAttempt, Long> {
    /**
     * @param id
     * @return
     */
    LoginAttempt findByID(Long id);
}
