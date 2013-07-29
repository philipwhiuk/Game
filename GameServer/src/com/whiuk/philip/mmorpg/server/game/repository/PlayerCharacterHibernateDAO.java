package com.whiuk.philip.mmorpg.server.game.repository;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.whiuk.philip.mmorpg.server.game.domain.PlayerCharacter;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateDAO;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;
import com.whiuk.philip.mmorpg.serverShared.Account;

/**
 * @author Philip Whitehouse
 */
@Repository
public class PlayerCharacterHibernateDAO
    extends HibernateDAO<PlayerCharacter, Long>
    implements PlayerCharacterDAO {

    @Override
    public final PlayerCharacter findByID(final Long id) {
        Session s = HibernateUtils.beginTransaction();
        PlayerCharacter character = null;
        String sql = "SELECT c FROM PlayerCharacter c WHERE c.id = :id";
        Query query = HibernateUtils.getSession().createQuery(sql)
                .setParameter("id", id);
        character = findOne(query);
        s.getTransaction().commit();
        return character;
    }

    @Override
    public List<PlayerCharacter> findByAccount(Account account) {
        List<PlayerCharacter> list;
        String sql = "SELECT c FROM PlayerCharacter c WHERE c.account = :account";
        Transaction tx = HibernateUtils.getSession().beginTransaction();
        Query query = HibernateUtils.getSession().createQuery(sql)
                .setParameter("account", account);
        list = findMany(query);
        tx.commit();
        return list;
    }

}
