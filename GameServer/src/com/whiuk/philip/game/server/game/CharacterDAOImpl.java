package com.whiuk.philip.game.server.game;

import org.hibernate.Query;

import com.whiuk.philip.game.server.hibernate.GenericDAOImpl;
import com.whiuk.philip.game.server.hibernate.HibernateUtils;

public class CharacterDAOImpl extends GenericDAOImpl<Character, Long> implements
		CharacterDAO {

	@Override
	public Character findByID(Long id) {
		Character character = null;
        String sql = "SELECT c FROM Character c WHERE c.id = :id";
        Query query = HibernateUtils.getSession()
        		.createQuery(sql).setParameter("id", id);
        character = findOne(query);
        return character;
	}

}
