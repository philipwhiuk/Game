package com.whiuk.philip.mmorpg.server.game.repository;

import java.util.List;

import com.whiuk.philip.mmorpg.server.game.domain.PlayerCharacter;
import com.whiuk.philip.mmorpg.server.hibernate.GenericDAO;
import com.whiuk.philip.mmorpg.serverShared.Account;

/**
 * Character Data Access Object.
 * 
 * @author Philip Whitehouse
 */
public interface PlayerCharacterDAO extends GenericDAO<PlayerCharacter, Long> {
    /**
     * @param id ID
     * @return character with the given ID or <code>null</code>.
     */
    PlayerCharacter findByID(Long id);
    /**
     * @param account account
     * @return List of characters controller by the account
     */
    List<PlayerCharacter> findByAccount(Account account);
}
