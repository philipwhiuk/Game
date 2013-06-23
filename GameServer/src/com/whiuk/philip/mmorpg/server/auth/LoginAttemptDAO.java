package com.whiuk.philip.mmorpg.server.auth;

import com.whiuk.philip.mmorpg.server.hibernate.GenericDAO;
import com.whiuk.philip.mmorpg.serverShared.LoginAttempt;

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
