package com.whiuk.philip.mmorpg.server.game.domain;


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
    final boolean executeAction() {
        zone.spawnCharacterByParent(p1, p2);
        return true;
    }
}
