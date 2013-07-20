package com.whiuk.philip.mmorpg.server.game.ai;

import java.util.List;


/**
 * 
 * @author Philip
 *
 */
public interface Goal {

    /**
     * @return list of requirements needed to achieve this goal
     */
    List<Requirement> getRequirements();

}
