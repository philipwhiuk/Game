package com.whiuk.philip.mmorpg.server.game.ai;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Set;


/**
 * An Agent is an intelligent computer system that is situated in
 * some {@link Environment}, and that is capable of autonomous
 * action ({@link AgentAction}s) in this {@link Environment}
 * in order to meet it's delegated objectives ({@link Goal}s).
 * <p>Description from:</p>
 * <p>
 * [Wooldridge and Jennings, 1995]
 * Intelligent agents: Theory and practice,
 * The Knowledge Engineering Review, 10(2):115-152
 * </p>
 * <p>via</p>
 * <p>
 * [Wooldridge, 2009]
 * An Introduction to MultiAgent Systems (John Wiley & Sons Ltd.)
 * </p>
 * <p>The actual implementation here draws on mainly deductive reasoning.
 * There's no use of beliefs or sensors here. It would be nice for the agent to
 * maintain state on the world and update it's internal model.</p>
 * <p>In practice this would likely involve a 'DerivedEnvironment' per Agent,
 * with each Agent's DerivedEnvironment being updated by triggers
 * from the real Environment. More intelligent agents would have more triggers
 * (as well as more {@link AgentAction}s.</p>
 * @author Philip
 *
 */
public abstract class Agent {
    /**
     * Environment.
     */
    private Environment environment;
    /**
     *
     */
    private Plan plan;
    /**
     *
     */
    private Set<Goal> goals;
    /**
     * @param e new environment
     */
    public final void setEnvironment(final Environment e) {
        environment = e;
    }
    /**
    * @return environment
    */
    public final Environment getEnvironment() {
        return environment;
    }

    /**
     * Update the agent.
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
     * @param action The action to perform
     * @return <code>true</code> if complete.
     */
    final boolean perform(final AgentAction action) {
        return action.perform();
    }
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
    final boolean canPerform(final AgentAction action) {
        //TODO Auto-generated method stub
        return true;
    }
    /**
     * Replans the agent's actions based on current goals.
     */
    private void replan() {
        //Turn it into an array and sort it
        Goal[] prioritisedGoals = goals.toArray(new Goal[goals.size()]);
         Arrays.sort(prioritisedGoals,
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
        for (Goal g: prioritisedGoals) {
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
