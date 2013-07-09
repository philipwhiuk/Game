package com.whiuk.philip.mmorpg.server.game.ai;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

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
        if(plan.getFirstAction() != null) {
            boolean completed = plan.getFirstAction().perform();
            if(completed) {
                plan.removeFirstAction();
            }
        }

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
        //Sort by importance
        Collections.sort(goals,new Comparator<Goal>() {
            @Override
            public int compare(Goal o1, Goal o2) {
                // TODO Auto-generated method stub
                return 0;
            }
        });
        Queue<AgentAction> fullList = new ArrayDeque<AgentAction>();
        for(Goal g: goals) {
            fullList.addAll(solve(g));
        }
        plan = new Plan();
        plan.setActions(fullList);
    }
    /**
     * Generate actions that solve a particular goal.
     * @param g
     * @return queue of actions
     */
    private Queue<AgentAction> solve(Goal g) {
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
            List<Requirement> targets) {
        // TODO Auto-generated method stub
        return null;
    }
}
