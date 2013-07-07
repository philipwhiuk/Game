package com.whiuk.philip.mmorpg.serverShared;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Philip Whitehouse
 */
@Entity
public class RegistrationAttempt {
    /**
     *
     */
    @Id @GeneratedValue
    private Long id;
    /**
     *
     */
    private Long time;
    /**
     *
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account")
    private Account account;
    /**
     *
     */
    private boolean successful;
    /**
     *
     */
    private String connection;
    /**
     * 
     */
    private String email;

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
    public final String getConnection() {
        return connection;
    }

    /**
     * @param c
     *            the connection to set
     */
    public final void setConnection(final String c) {
        this.connection = c;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
       this.email = email;
    }

}
