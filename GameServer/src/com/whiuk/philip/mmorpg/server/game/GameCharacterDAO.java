package com.whiuk.philip.mmorpg.server.game;

import com.whiuk.philip.mmorpg.server.hibernate.GenericDAO;

/**
 * Character Data Access Object.
 * 
 * @author Philip Whitehouse
 */
public interface GameCharacterDAO extends GenericDAO<PlayerCharacter, Long> {
    /**
     * @param id
     * @return
     */
    PlayerCharacter findByID(Long id);
}
