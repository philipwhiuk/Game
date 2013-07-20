package com.whiuk.philip.mmorpg.server.game.domain;


/**
 * A single character in the game world.
 * @author Philip
 *
 */
public interface GameCharacter extends RelatableEntity {

    /**
     * 
     * @param i1id
     * @return
     */
    Item getItemById(int i1id);

    /**
     * 
     * @param a
     * @return
     */
    boolean canPerform(Action a);

    /**
     * 
     * @param a
     */
    void doAction(Action a);

    /**
     * 
     * @return
     */
    String getName();

    /**
     * 
     * @return
     */
    Race getRace();

    /**
     * 
     * @return ID of the zone
     */
    Long getZone();

}
