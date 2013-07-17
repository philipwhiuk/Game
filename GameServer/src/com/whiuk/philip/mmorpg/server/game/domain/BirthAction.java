package com.whiuk.philip.mmorpg.server.game.domain;

import com.whiuk.philip.mmorpg.server.game.controller.ZoneController;


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
    private ZoneController zoneController;
    /**
     * 
     */
    private Zone zone;

    @Override
    final boolean executeAction() {
        zoneController.spawnNPCByParent(zone, p1, p2);
        return true;
    }
}
