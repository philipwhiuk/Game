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
    public void setLastLoginAttempt(LoginAttempt attempt) {
        lastLoginAttempt = attempt;
    }

    /**
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param p
     *            password
     */
    public void setPassword(String p) {
        password = p;
    }

    /**
     * @return
     */
    public String getUsername() {
        // TODO Auto-generated method stub
        return username;
    }

    /**
     * @param u
     *            username
     */
    public void setUsername(String u) {
        username = u;
    }
}
