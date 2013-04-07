package com.whiuk.philip.game.server.game;

import com.whiuk.philip.game.server.hibernate.GenericDAO;

/**
 * Character Data Access Object.
 * 
 * @author Philip Whitehouse
 */
public interface CharacterDAO extends GenericDAO<Character, Long> {
    /**
     * @param id
     * @return
     */
    Character findByID(Long id);
}
