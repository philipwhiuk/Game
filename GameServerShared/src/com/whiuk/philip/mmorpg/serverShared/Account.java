package com.whiuk.philip.mmorpg.serverShared;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
    @Id @GeneratedValue
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
     * 
     */
    private String email;
    /**
     * 
     */
    private boolean emailInvalid;

    public Account() {
        loginAttempts = new HashSet<LoginAttempt>();
    }
    
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
        return username;
    }

    /**
     * @param u
     *            username
     */
    public final void setUsername(final String u) {
        username = u;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @param b true if the email address is invalid.
     */
    public void setEmailInvalid(boolean b) {
        this.emailInvalid = b;
    }

    public Set<LoginAttempt> getLoginAttempts() {
        return loginAttempts;
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Account)) {
            return false;
        }
        Account other = (Account) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
