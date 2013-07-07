package com.whiuk.philip.mmorpg.server.auth;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.whiuk.philip.mmorpg.server.hibernate.GenericDAOImpl;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;
import com.whiuk.philip.mmorpg.serverShared.LoginAttempt;

/**
 * Hibernate Login Attempts Data Access Object.
 * @author Philip Whitehouse
 */
@Repository
public class LoginAttemptDAOImpl extends GenericDAOImpl<LoginAttempt, Long>
        implements LoginAttemptDAO {
    @Override
    public final LoginAttempt findByID(final Long id) {
        LoginAttempt loginAttempt = null;
        String sql = "SELECT la FROM LoginAttempt la WHERE la.id = :id";
        Query query = HibernateUtils.getSession().createQuery(sql)
                .setParameter("id", id);
        loginAttempt = findOne(query);
        return loginAttempt;
    }
}
