package com.whiuk.philip.mmorpg.server.game.ai;
import java.util.List;

import com.whiuk.philip.mmorpg.server.game.Goal;


public abstract class Agent {
    Plan plan;
    List<Goal> goals;
    
    void update() {
        if (planIsInvalid()) {
            replan();
        }
        plan.getNextAction().perform();

    }

    private boolean planIsInvalid() {
        // TODO Auto-generated method stub
        return false;
    }

    private void replan() {
        // TODO Auto-generated method stub
        
    }
}
