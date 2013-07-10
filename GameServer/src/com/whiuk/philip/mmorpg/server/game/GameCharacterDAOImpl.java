package com.whiuk.philip.mmorpg.server.game;

import org.hibernate.Query;

import com.whiuk.philip.mmorpg.server.hibernate.GenericDAOImpl;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;

/**
 * @author Philip Whitehouse
 */
public class GameCharacterDAOImpl
    extends GenericDAOImpl<PlayerCharacter, Long>
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
