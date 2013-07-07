package com.whiuk.philip.mmorpg.server.game;

/**
 * 
 * @author Philip
 *
 */
public class BirthAction extends MultiTurnEndAction {
    /**
     * 
     */
    private NPC p1;
    /**
     * 
     */
    private NPC p2;
    /**
     * 
     */
    private Zone zone;

    @Override
    boolean executeAction() {
        zone.spawnCharacterByParent(p1, p2);
        return true;
    }
}
