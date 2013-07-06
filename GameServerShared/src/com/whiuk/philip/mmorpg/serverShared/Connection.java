package com.whiuk.philip.mmorpg.serverShared;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;

/**
 * Information about a connection.
 * 
 * @author Philip Whitehouse
 */
@Entity
public class Connection {
    /**
     * 
     */
    @Id
    private long id;
    /**
	 *
	 */
    private long lastConnectionTime;
    /**
	 *
	 */
    private boolean active;
    /**
	 *
	 */
    private ClientInfo clientInfo;
    /**
	 *
	 */
    @OneToMany(mappedBy = "connection")
    private Set<LoginAttempt> loginAttempts;

    /**
     * Bean constructor.
     */
    public Connection() {
    }

    /**
     * @param c
     * @param nanoTime
     * @param b
     */
    public Connection(final ClientInfo c, final long nanoTime, final boolean b) {
        this.clientInfo = c;
        this.lastConnectionTime = nanoTime;
        this.active = b;
    }

    /**
	 *
	 */
    public final void setActive(final boolean a) {
        active = a;
    }

    /**
     * @param attempt
     */
    public final void addLoginAttempt(final LoginAttempt attempt) {
        this.loginAttempts.add(attempt);
    }

    /**
     * @param nanoTime
     */
    public final void setLastConnectionTime(final long nanoTime) {
        lastConnectionTime = nanoTime;
    }

}
