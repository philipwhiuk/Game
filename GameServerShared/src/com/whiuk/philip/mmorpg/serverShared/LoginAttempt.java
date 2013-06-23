package com.whiuk.philip.mmorpg.serverShared;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.whiuk.philip.mmorpg.serverShared.Connection;

/**
 * @author Philip Whitehouse
 */
@Entity
public class LoginAttempt {
    /**
     *
     */
    @Id
    private Long id;
    /**
     *
     */
    private Long time;
    /**
     *
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private Account account;
    /**
     *
     */
    private boolean successful;
    /**
     *
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private Connection connection;

    /**
     * @return the time
     */
    public final Long getTime() {
        return time;
    }

    /**
     * @param t
     *            the time to set
     */
    public final void setTime(final Long t) {
        this.time = t;
    }

    /**
     * @return the account
     */
    public final Account getAccount() {
        return account;
    }

    /**
     * @param a
     *            the account to set
     */
    public final void setAccount(final Account a) {
        this.account = a;
    }

    /**
     * @return whether successful
     */
    public final boolean isSuccessful() {
        return successful;
    }

    /**
     * @param s
     *            set as successful or not
     */
    public final void setSuccessful(final boolean s) {
        this.successful = s;
    }

    /**
     * @return the connection
     */
    public final Connection getConnection() {
        return connection;
    }

    /**
     * @param c
     *            the connection to set
     */
    public final void setConnection(final Connection c) {
        this.connection = c;
    }

}
