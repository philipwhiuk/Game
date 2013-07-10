package com.whiuk.philip.mmorpg.server.game.domain;

import com.whiuk.philip.mmorpg.server.game.ai.AgentAction;

/**
 *
 * @author Philip
 *
 */
public abstract class MultiTurnEndAction implements AgentAction {

    /**
     *
     */
    private int turnsRemaining;

    @Override
    public final boolean perform() {
        if (turnsRemaining == 0 || turnsRemaining-- == 0) {
            return executeAction();
        } else {
            return false;
        }
    }

    /**
     *
     * @return whether the action has been completed.
     */
    abstract boolean executeAction();

}
