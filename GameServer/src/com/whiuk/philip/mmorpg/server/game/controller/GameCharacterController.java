package com.whiuk.philip.mmorpg.server.game.controller;

import com.whiuk.philip.mmorpg.server.game.domain.GameCharacter;
import com.whiuk.philip.mmorpg.server.game.domain.PlayerCharacter;
import com.whiuk.philip.mmorpg.server.game.domain.Tile;

/**
 * 
 * @author Philip
 *
 */
public interface GameCharacterController {
    /**
     * 
     * @param character
     * @param source
     * @param target
     */
    public void smith(final GameCharacter character,
            final int source, final int target);

    /**
     * 
     * @param character
     * @param source
     * @param target
     */
    public void mine(final GameCharacter character,
            final int source, final int target);

    /**
     * 
     * @param character
     * @param source
     * @param target
     */
    public void craft(final GameCharacter character,
            final int source, final int target);

    /**
     * 
     * @param character
     * @param source
     */
    public void take(GameCharacter character, int source);

    /**
     * 
     * @param character
     * @param source
     */
    public void drop(GameCharacter character, int source);

    /**
     * 
     * @param character
     * @param source
     */
    public void equip(GameCharacter character, int source);

    /**
     * 
     * @param character
     * @param source
     */
    public void examine(GameCharacter character, int source);

    /**
     * 
     * @param character
     * @param source
     */
    public void unequip(GameCharacter character, int source);

    /**
     * 
     * @param character
     * @param source
     * @param target
     */
    public void cast(GameCharacter character, int source, int target);

    /**
     * 
     * @param character
     * @param i1ID
     * @param i2ID
     */
    public void use(final GameCharacter character,
            final int i1ID, final int i2ID);

    /**
     * @param data
     */
    public void sendZoneData(GameCharacter character, Tile[][] data);
}
