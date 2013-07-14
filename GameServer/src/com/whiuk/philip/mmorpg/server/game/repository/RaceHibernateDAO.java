package com.whiuk.philip.mmorpg.server.game.repository;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.whiuk.philip.mmorpg.server.game.domain.Race;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateDAO;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;

/**
 * @author Philip Whitehouse
 */
@Repository
public class RaceHibernateDAO
    extends HibernateDAO<Race, Long>
    implements RaceDAO {

    @Override
    public final Race findByID(final Long id) {
        Race character = null;
        String sql = "SELECT r FROM Race r WHERE r.id = :id";
        Query query = HibernateUtils.getSession().createQuery(sql)
                .setParameter("id", id);
        character = findOne(query);
        return character;
    }

    @Override
    public final Race findByName(final String name) {
        Race character = null;
        Session s = HibernateUtils.beginTransaction();
        String sql = "SELECT r FROM Race r WHERE r.name = :name";
        Query query = HibernateUtils.getSession().createQuery(sql)
                .setParameter("name", name);
        character = findOne(query);
        s.getTransaction().commit();
        return character;
    }

}
