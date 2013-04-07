package com.whiuk.philip.game.server.auth;

import javax.persistence.Entity;

import com.whiuk.philip.game.server.system.Connection;

/**
 *
 * @author Philip
 *
 */
@Entity
public class Account {

	private long lastLoginAttempt;
	private String password;

	public void setLastLoginAttempt(long nanoTime) {
		lastLoginAttempt = nanoTime;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

}
