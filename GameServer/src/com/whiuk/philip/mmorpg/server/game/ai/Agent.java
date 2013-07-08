package com.whiuk.philip.mmorpg.server.game.ai;
import java.util.List;

import com.whiuk.philip.mmorpg.server.game.Goal;

/**
 *
 * @author Philip
 *
 */
public abstract class Agent {
    /**
     *
     */
    private Plan plan;
    /**
     *
     */
    private List<Goal> goals;
    /**
     *
     */
    public final void update() {
        if (planIsInvalid()) {
            replan();
        }
        plan.getNextAction().perform();

    }
    /**
     *
     * @return
     */
    private boolean planIsInvalid() {
        // TODO Auto-generated method stub
        return false;
    }
    /**
     *
     */
    private void replan() {
        // TODO Auto-generated method stub
    }
}
