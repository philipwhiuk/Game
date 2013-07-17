package com.whiuk.philip.mmorpg.server.game.controller;

import com.whiuk.philip.mmorpg.server.game.domain.GameCharacter;
import com.whiuk.philip.mmorpg.server.game.domain.NPC;
import com.whiuk.philip.mmorpg.server.game.domain.Zone;

/**
 * 
 * @author Philip
 *
 */
public interface ZoneController {

    /**
     * 
     * @param gc
     * @param zone
     */
    void characterEntered(final Zone zone, final GameCharacter gc);
    /**
     * 
     * @param zone
     * @param npc1
     * @param npc2
     */
    void spawnNPCByParent(final Zone zone, final NPC npc1, final NPC npc2);

}
