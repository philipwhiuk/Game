package com.whiuk.philip.mmorpg.server.game.ai;

/**
 * A message that can be understood by an agent.
 * <p>Loosely inspired by JADE's
 * <a href="http://jade.tilab.com/doc/api/jade/lang/acl/ACLMessage.html">
 * ACLMessage</a>.</p>
 * @author Philip
 *
 */
public interface AgentMessage {
    /**
     * 
     * @author Philip
     *
     */
    public enum Type {
        /**
         * 
         */
        INFORM,
        /**
         * 
         */
        REQUEST,
        /**
         * 
         */
        OFFER,
        /**
         * 
         */
        PROMISE,
        /**
         * 
         */
        DECLINE;
    }
    /**
     * 
     * @param a Agent
     */
    void setSource(Agent a);
    /**
     * 
     * @return Agent
     */
    Agent getSource();
    /**
     * 
     * @param a Agent
     */
    void setDestination(Agent a);
    /**
     * 
     * @return Agent
     */
    Agent getDestination();
    /**
     * 
     * @param m message
     */
    void setMessage(byte[] m);
    /**
     * @return message
     */
    byte[] getMessage();
    /**
     * 
     * @return message type
     */
    Type getType();
}
