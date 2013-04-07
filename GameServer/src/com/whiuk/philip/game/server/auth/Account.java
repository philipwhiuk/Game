package com.whiuk.philip.game.server.auth;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.whiuk.philip.game.server.system.Connection;

/**
 *
 * @author Philip
 *
 */
@Entity
public class Account {

	@Id
	private Long id;
	private String username;
	private long lastLoginAttempt;
	private String password;

	/**
	 * 
	 * @param nanoTime
	 */
	public void setLastLoginAttempt(long nanoTime) {
		lastLoginAttempt = nanoTime;
	}

	/**
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @return
	 */
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

}
