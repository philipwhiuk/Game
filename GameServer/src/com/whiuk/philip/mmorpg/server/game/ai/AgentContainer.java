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
     * @param a Agent
     */
    void registerAgent(Agent a);
    /**
     * De-register the agent from this container.
     * @param a Agent
     */
    void deregisterAgent(Agent a);
    /**
     * Transfer this agent to the new container.
     * Note this provides an atomic transfer operation,
     * rather than risk allow an agent be double registered.
     * @param a Agent
     * @param c Container
     */
    void transferAgent(Agent a, AgentContainer c);
    /**
     * Get the next message for the agent.
     * @param a Agent
     * @return message or <code>null</code>
     */
    AgentMessage getMessageForAgent(Agent a);
    /**
     * 
     * @param m Message
     */
    void putMessageForAgent(AgentMessage m);
    /**
     * Check to see if there are any messages for the agent.
     * @param a Agent
     * @return <code>true</code> if there are outstanding
     *          messages for the given agent
     */
    boolean hasMessageForAgent(Agent a);
}
