package com.whiuk.philip.mmorpg.server.auth;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.whiuk.philip.mmorpg.server.hibernate.GenericDAOImpl;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;
import com.whiuk.philip.mmorpg.serverShared.RegistrationAttempt;

/**
 * Hibernate Registration Attempt Data Access Object.
 * @author Philip Whitehouse
 */
@Repository
public class RegistrationAttemptDAOImpl
        extends GenericDAOImpl<RegistrationAttempt, Long>
        implements RegistrationAttemptDAO {
    @Override
    public final RegistrationAttempt findByID(final Long id) {
        RegistrationAttempt registrationAttempt = null;
        String sql = "SELECT la FROM RegistrationAttempt ra WHERE ra.id = :id";
        Query query = HibernateUtils.getSession().createQuery(sql)
                .setParameter("id", id);
        registrationAttempt = findOne(query);
        return registrationAttempt;
    }
}
