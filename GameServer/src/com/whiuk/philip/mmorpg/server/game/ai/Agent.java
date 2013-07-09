package com.whiuk.philip.mmorpg.server.game.ai;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Set;

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
    private Set<Goal> goals;
    /**
     *
     */
    public final void update() {
        if (!planIsValid()) {
            replan();
        }
        if (plan.getFirstAction() != null) {
            boolean completed = perform(plan.getFirstAction());
            if (completed) {
                plan.removeFirstAction();
            }
        }
    }
    /**
     * Perform an action.
     * @param firstAction
     * @return <code>true</code> if complete.
     */
    abstract boolean perform(AgentAction firstAction);
    /**
     * Checks whether the plan is still valid.
     * @return <code>true</code> if valid.
     */
    private boolean planIsValid() {
        return canPerform(plan.getFirstAction());
        //TODO: Should probably check if goals are still desirable.
    }
    /**
     * Checks whether the action can be done.
     * @param firstAction
     * @return <code>true</code> if it can be done.
     */
    abstract boolean canPerform(AgentAction action);
    /**
     * Replans the agent's actions based on current goals.
     */
    private void replan() {
        //Turn it into an array and sort it
        Goal[] priortisedGoals = goals.toArray(new Goal[goals.size()]);
         Arrays.sort(priortisedGoals,
                new Comparator<Goal>() {
            @Override
            public int compare(final Goal o1, final Goal o2) {
                // TODO Auto-generated method stub
                return 0;
            }
        });
        /*
         * For each goal, queue the actions needed to solve it.
         * Very naive. Assumes goals are independent
         * (which they never will be in practive).
         * TODO: Improve approach to solving multiple goals.
         */
        Queue<AgentAction> fullList = new ArrayDeque<AgentAction>();
        for (Goal g: priortisedGoals) {
            fullList.addAll(solve(g));
        }
        plan = new Plan();
        plan.setActions(fullList);
        plan.setGoals(goals);
    }
    /**
     * Generate actions that solve a particular goal.
     * @param g
     * @return queue of actions
     */
    private Queue<AgentAction> solve(final Goal g) {
        List<Requirement> requirements = g.getRequirements();
        List<Requirement> targets = new ArrayList<Requirement>();
        boolean completed = true;
        for (Requirement r: requirements) {
            if (!r.satisifiedBy(this)) {
                completed = false;
            } else {
                targets.add(r);
            }
        }
        if (!completed) {
            return determineActionsForRequirements(targets);
        }
        return null;
    }
    /**
     * Generate actions that achieve a set of requirements.
     * @param g
     * @return ordered list of actions to meet requirements
     */
    private Queue<AgentAction> determineActionsForRequirements(
            final List<Requirement> targets) {
        // TODO Auto-generated method stub
        return null;
    }
}
