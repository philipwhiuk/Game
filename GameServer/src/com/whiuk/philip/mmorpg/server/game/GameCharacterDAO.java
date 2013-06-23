package com.whiuk.philip.mmorpg.server.game;

import com.whiuk.philip.mmorpg.server.hibernate.GenericDAO;

/**
 * Character Data Access Object.
 * 
 * @author Philip Whitehouse
 */
public interface GameCharacterDAO extends GenericDAO<GameCharacter, Long> {
    /**
     * @param id
     * @return
     */
    GameCharacter findByID(Long id);
}
