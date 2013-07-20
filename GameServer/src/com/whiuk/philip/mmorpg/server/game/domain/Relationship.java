package com.whiuk.philip.mmorpg.server.game.domain;

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
    /**
     * Party 1.
     */
    private RelatableEntity p1;
    /**
     * Party 2.
     */
    private RelatableEntity p2;
}
