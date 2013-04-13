package com.whiuk.philip.game.server.auth;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Philip
 */
@Entity
public class Account {

    /**
     *
     */
    @Id
    private Long id;
    /**
     *
     */
    private String username;
    /**
     *
     */
    private LoginAttempt lastLoginAttempt;
    /**
     *
     */
    private String password;

    /**
     * @param nanoTime
     */
    public final void setLastLoginAttempt(final LoginAttempt attempt) {
        lastLoginAttempt = attempt;
    }

    /**
     * @return
     */
    public final String getPassword() {
        return password;
    }

    /**
     * @param p
     *            password
     */
    public final void setPassword(final String p) {
        password = p;
    }

    /**
     * @return
     */
    public final String getUsername() {
        // TODO Auto-generated method stub
        return username;
    }

    /**
     * @param u
     *            username
     */
    public final void setUsername(final String u) {
        username = u;
    }
}
