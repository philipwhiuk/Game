package com.whiuk.philip.mmorpg.server.game.controller;

import com.whiuk.philip.mmorpg.server.game.domain.GameCharacter;
import com.whiuk.philip.mmorpg.server.game.domain.PlayerCharacter;

/**
 * 
 * @author Philip
 *
 */
public interface GameCharacterController {
    /**
     * 
     * @param c character
     * @param s source
     * @param t target
     */
    public void smith(final GameCharacter c,
            final int s, final int t);

    /**
     * 
     * @param character Character
     * @param source Source
     * @param target Target
     */
    public void mine(final GameCharacter character,
            final int source, final int target);

    /**
     * 
     * @param character Character
     * @param source Source
     * @param target Target
     */
    public void craft(final GameCharacter character,
            final int source, final int target);

    /**
     * 
     * @param character Character
     * @param source Source
     */
    public void take(GameCharacter character, int source);

    /**
     * 
     * @param character Character
     * @param source Source
     */
    public void drop(GameCharacter character, int source);

    /**
     * 
     * @param character Character
     * @param source Source
     */
    public void equip(GameCharacter character, int source);

    /**
     * 
     * @param character Character
     * @param source Source
     */
    public void examine(GameCharacter character, int source);

    /**
     * 
     * @param character Character
     * @param source Source
     */
    public void unequip(GameCharacter character, int source);

    /**
     * 
     * @param character Character
     * @param source Source
     * @param target Target
     */
    public void cast(GameCharacter character, int source, int target);

    /**
     * 
     * @param c character
     * @param i1 ID of item 1
     * @param i2 ID of item 2
     */
    public void use(final GameCharacter c,
            final int i1, final int i2);

    /**
     * @param playerCharacter Player character
     */
    public void handleExit(PlayerCharacter playerCharacter);
}
