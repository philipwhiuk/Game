package com.whiuk.philip.mmorpg.server.game.repository;

import java.util.List;

import com.whiuk.philip.mmorpg.server.game.domain.Race;
import com.whiuk.philip.mmorpg.server.hibernate.GenericDAO;

public interface RaceDAO extends GenericDAO<Race, Long> {
    /**
     * @param id ID
     * @return Race
     */
    Race findByID(Long id);
    /**
     * @param name name
     * @return Race
     */
    Race findByName(String name);

}
