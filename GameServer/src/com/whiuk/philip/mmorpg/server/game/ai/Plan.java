package com.whiuk.philip.mmorpg.server.game.ai;
import java.util.Queue;
import java.util.Set;

import com.whiuk.philip.mmorpg.server.game.Goal;

/**
 * A sequence of {@link AgentAction}s that
 * should achieve a {@link Set} of {@link Goal}s.
 * @author Philip
 *
 */
public class Plan {
    /**
     * The actions this plan will perform.
     */
    private Queue<AgentAction> actions;
    /**
     * The goals this plan aims to achieve.
     */
    private Set<Goal> goals;

    /**
     * @return the first action to perform
     */
    public final AgentAction getFirstAction() {
        return actions.peek();
    }

    /**
     * @param a actions
     */
    public final void setActions(final Queue<AgentAction> a) {
        this.actions = a;
    }

    /**
     * Remove the first action from the queue.
     */
    public final void removeFirstAction() {
        actions.remove();
    }

    /**
     * @param goals the goals this plan will achieve.
     */
    public final void setGoals(final Set<Goal> goals) {
        this.goals = goals;
    }
}
