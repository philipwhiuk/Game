package com.whiuk.philip.mmorpg.server.game.repository;

import org.hibernate.Query;

import com.whiuk.philip.mmorpg.server.game.domain.PlayerCharacter;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateDAO;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;

/**
 * @author Philip Whitehouse
 */
public class GameCharacterHibernateDAO
    extends HibernateDAO<PlayerCharacter, Long>
    implements GameCharacterDAO {

    @Override
    public final PlayerCharacter findByID(final Long id) {
        PlayerCharacter character = null;
        String sql = "SELECT c FROM Character c WHERE c.id = :id";
        Query query = HibernateUtils.getSession().createQuery(sql)
                .setParameter("id", id);
        character = findOne(query);
        return character;
    }

}
