package com.whiuk.philip.game.server.auth;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.whiuk.philip.game.server.hibernate.GenericDAOImpl;
import com.whiuk.philip.game.server.hibernate.HibernateUtils;

/**
 * Hibernate Accounts Data Access Object
 * 
 * @author Philip Whitehouse
 */
@Repository
public class LoginAttemptDAOImpl extends GenericDAOImpl<LoginAttempt, Long>
        implements LoginAttemptDAO {
    /**
     * Retrieves an account by it's id.
     * 
     * @param username
     * @return
     */
    @Override
    public LoginAttempt findByID(final Long id) {
        LoginAttempt loginAttempt = null;
        String sql = "SELECT la FROM LoginAttempt la WHERE la.id = :id";
        Query query = HibernateUtils.getSession().createQuery(sql)
                .setParameter("id", id);
        loginAttempt = findOne(query);
        return loginAttempt;
    }
}
