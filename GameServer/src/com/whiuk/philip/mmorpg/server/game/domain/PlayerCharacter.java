package com.whiuk.philip.mmorpg.server.game.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
    @GeneratedValue
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
     * 
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "race")
    private Race race;
    /**
     * Name
     */
    private String name;
    /**
     * Zone ID.
     */
    private Long zone;

    /**
     * Default constructor
     */
    public PlayerCharacter() {
    }

    /**
     * Creation constructor
     * @param account 
     * @param name
     * @param race
     */
    public PlayerCharacter(final Account account,
            final String name, final Race race) {
        this.account = account;
        this.name = name;
        this.race = race;
    }

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
    public final String getName() {
         return name;
    }

    @Override
    public final Race getRace() {
        return race;
    }

    @Override
    public Long getZone() {
        return zone;
    }

    public Account getAccount() {
        return account;
    }

    public Long getId() {
        return id;
    }

}
