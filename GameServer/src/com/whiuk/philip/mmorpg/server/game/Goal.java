package com.whiuk.philip.mmorpg.server.game;

import java.util.List;

import com.whiuk.philip.mmorpg.server.game.ai.Requirement;

/**
 * 
 * @author Philip
 *
 */
public interface Goal {

    /**
     * 
     * @return
     */
    List<Requirement> getRequirements();

}
