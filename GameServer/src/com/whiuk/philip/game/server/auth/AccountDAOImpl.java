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
public class AccountDAOImpl extends GenericDAOImpl<Account, Long> implements
        AccountDAO {
    /**
     * Retrieves an account by it's id.
     * 
     * @param username
     * @return
     */
    @Override
    public final Account findByID(final Long id) {
        Account account = null;
        String sql = "SELECT a FROM Account a WHERE a.id = :id";
        Query query = HibernateUtils.getSession().createQuery(sql)
                .setParameter("id", id);
        account = findOne(query);
        return account;
    }

    /**
     * Retrieves an account by it's username.
     * 
     * @param username
     * @return
     */
    public final Account findByUsername(final String username) {
        Account account = null;
        String sql = "SELECT a FROM Account a WHERE a.username = :username";
        Query query = HibernateUtils.getSession().createQuery(sql)
                .setParameter("username", username);
        account = findOne(query);
        return account;
    }
}
