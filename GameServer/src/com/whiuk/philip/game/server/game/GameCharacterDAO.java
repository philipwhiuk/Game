package com.whiuk.philip.game.server.game;

import com.whiuk.philip.game.server.hibernate.GenericDAO;

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
