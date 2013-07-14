package com.whiuk.philip.mmorpg.server.game.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.whiuk.philip.mmorpg.serverShared.Account;


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
     * 
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "account")
    private Account account;

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

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Race getRace() {
        // TODO Auto-generated method stub
        return null;
    }

}
