package com.whiuk.philip.mmorpg.server.game.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;


/**
 * Hibernate Character Entity.
 * 
 * @author Philip Whitehouse
 */
@Entity
public class PlayerCharacter implements GameCharacter {

    /**
     * 
     */
    @Id
    private Long id;

    /**
     * Whether the player controlling the character has logged out. See {@link
     * logout()}.
     */
    @Transient
    private boolean loggedOut;

    /**
     * Indicates the player in control of the character has logged out and the
     * character should be removed from the world once any activity has ceased.
     */
    public final void logout() {
        this.loggedOut = true;
    }

    @Override
    public final boolean canPerform(final Action a) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public final Item getItemById(final int i2id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void doAction(final Action a) {
        // TODO Auto-generated method stub
        
    }

}
