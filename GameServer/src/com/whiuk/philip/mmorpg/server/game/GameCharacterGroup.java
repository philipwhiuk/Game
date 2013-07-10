package com.whiuk.philip.mmorpg.server.game;

import java.util.Set;

/**
 * A group of game characters.
 * @author Philip
 *
 */
public interface GameCharacterGroup extends RelatableEntity {
    /**
     * Add a character to the group.
     * @param newMember The character to add
     */
    void add(GameCharacter newMember);
    /**
     * Remove a character from the group.
     * @param oldMember The character to remove
     */
    void remove(GameCharacter oldMember);
}
