package com.whiuk.philip.mmorpg.serverShared;

import java.util.HashSet;
import java.util.Set;

import com.whiuk.philip.mmorpg.shared.Messages.ClientInfo;

/**
 * Information about a connection.
 * 
 * @author Philip Whitehouse
 */
public class Connection {
    /**
     * 
     */
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
    private Set<LoginAttempt> loginAttempts;
    /**
    *
    */
    private Set<RegistrationAttempt> registrationAttempts;

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
        loginAttempts = new HashSet<LoginAttempt>();
        registrationAttempts = new HashSet<RegistrationAttempt>();
    }

    /**
     *
     */
    public final boolean isActive() {
        return active;
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
        loginAttempts.add(attempt);
    }

    /**
     * @param nanoTime
     */
    public final void setLastConnectionTime(final long nanoTime) {
        lastConnectionTime = nanoTime;
    }
    
    @Override
    public String toString() {
        return "Local:"+clientInfo.getLocalIPAddress() + "\n"
                + "Remote:"+clientInfo.getRemoteIPAddress() + "\n"
                + "Client:"+clientInfo.getClientID() + " \n"
                + "ConnectionTime:"+lastConnectionTime;
    }

    /**
     * 
     * @return
     */
    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    /**
     * 
     * @param attempt
     */
    public void addRegistrationAttempt(RegistrationAttempt attempt) {
        registrationAttempts.add(attempt);
        
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
}
