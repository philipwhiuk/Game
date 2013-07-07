package com.whiuk.philip.mmorpg.server.auth;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.whiuk.philip.mmorpg.server.hibernate.GenericDAOImpl;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;
import com.whiuk.philip.mmorpg.serverShared.Account;

/**
 * Hibernate Accounts Data Access Object.
 * @author Philip Whitehouse
 */
@Repository
public class AccountDAOImpl extends GenericDAOImpl<Account, Long> implements
        AccountDAO {
    /**
     * Retrieves an account by it's id.
     * @param id ID
     * @return Account
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
     * @param username Username
     * @return Account
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
