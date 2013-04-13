package com.whiuk.philip.game.server.game;

import javax.persistence.Entity;

/**
 * Hibernate Character Entity.
 * 
 * @author Philip Whitehouse
 */
@Entity
public class GameCharacter {

    /**
     * Whether the player controlling the character has logged out. See {@link
     * logout()}.
     */
    private boolean loggedOut;

    /**
     * Indicates the player in control of the character has logged out and the
     * character should be removed from the world once any activity has ceased.
     */
    public final void logout() {
        this.loggedOut = true;
    }

}
