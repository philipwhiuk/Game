package com.whiuk.philip.mmorpg.server.game.ai;

/**
 * Provides processing and support for agents.
 * Note that this separate from the {@link Agent}'s {@link Environment}.
 * @author Philip
 *
 */
public interface AgentContainer {
    /**
     * Register the agent to this container.
     * @param a
     */
    void registerAgent(Agent a);
    /**
     * De-register the agent from this container.
     * @param a
     */
    void deregisterAgent(Agent a);
    /**
     * Transfer this agent to the new container.
     * 
     * Note this provides an atomic transfer operation,
     * rather than risk allow an agent be double registered.
     */
    void transferAgent(Agent a, AgentContainer c);
    /**
     * 
     * @param a
     * @return message
     */
    AgentMessage getMessageForAgent(Agent a);
    /**
     * 
     * @param m
     */
    void putMessageForAgent(AgentMessage m);
    /**
     * 
     * @param a Agent
     * @return <code>true if there are outstanding messages for the given agent
     */
    boolean hasMessageForAgent(Agent a);
}
