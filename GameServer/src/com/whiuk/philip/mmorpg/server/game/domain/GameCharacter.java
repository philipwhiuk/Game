package com.whiuk.philip.mmorpg.server.game.domain;


/**
 * A single character in the game world.
 * @author Philip
 *
 */
public interface GameCharacter extends RelatableEntity {

    /**
     * 
     * @param i ID
     * @return item
     */
    Item getItemById(int i);

    /**
     * @param a Action
     * @return <code>true</code> if the character can perform the given action
     */
    boolean canPerform(Action a);

    /**
     * @param a Action
     */
    void doAction(Action a);

    /**
     * @return name
     */
    String getName();

    /**
     * @return race
     */
    Race getRace();

    /**
     * 
     * @return ID of the zone
     */
    Long getZone();

}
