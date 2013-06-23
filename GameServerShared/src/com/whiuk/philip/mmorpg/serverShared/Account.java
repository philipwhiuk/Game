package com.whiuk.philip.mmorpg.serverShared;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
    @OneToMany(mappedBy = "account")
    private Set<LoginAttempt> loginAttempts;
    /**
     *
     */
    private String password;

    /**
     * @param nanoTime
     */
    public final void addLoginAttempt(final LoginAttempt attempt) {
        loginAttempts.add(attempt);
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
