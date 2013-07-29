package com.whiuk.philip.mmorpg.server.auth.repository;

import com.whiuk.philip.mmorpg.server.hibernate.GenericDAO;
import com.whiuk.philip.mmorpg.serverShared.RegistrationAttempt;

/**
 * Account data access object interface.
 * @author Philip Whitehouse
 */
public interface RegistrationAttemptDAO
    extends GenericDAO<RegistrationAttempt, Long> {
    /**
     * @param id ID
     * @return Registration Attempt
     */
    RegistrationAttempt findByID(Long id);
}
