package com.whiuk.philip.mmorpg.server.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.whiuk.philip.mmorpg.server.game.domain.GameCharacter;
import com.whiuk.philip.mmorpg.server.game.domain.NPC;
import com.whiuk.philip.mmorpg.server.game.domain.Zone;

/**
 * 
 * @author Philip
 *
 */

@Controller
public class ZoneControllerImpl implements ZoneController {
    @Autowired
    private GameCharacterController gameCharacterController;

    /**
     * 
     */
    public ZoneControllerImpl() {
    }

    @Override
    public final void characterEntered(final Zone zone, final GameCharacter gc) {
        zone.addCharacter(gc);
        gameCharacterController.sendZoneData(gc, zone.getData());
        for (GameCharacter character : zone.getCharacters()) {
            notifyCharacterEntered(character, gc);
        }
    }

    private void notifyCharacterEntered(GameCharacter character,
            GameCharacter gc) {
        // TODO Auto-generated method stub
    }

    @Override
    public final void spawnNPCByParent(final Zone zone, final NPC p1, final NPC p2) {
        // TODO Auto-generated method stub
        NPC p3 = new NPC();
        characterEntered(zone, p3);
    }

}
