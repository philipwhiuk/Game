package com.whiuk.philip.mmorpg.server.game;

/**
 * A bi-directional association between two entities.
 * @author Philip
 *
 */
public class Relationship {
    /**
     * 
     * @author Philip
     *
     */
    public enum State {
        NEUTRAL, FRIENDLY, HOSTILE, WAR, ALLIES;
    }
    /**
     * The state of the relationship.
     */
    private State state;
}
