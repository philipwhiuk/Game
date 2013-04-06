package com.whiuk.philip.game.server.system;

import com.whiuk.philip.game.shared.Messages.ClientInfo;

/**
 * Information about a connection.
 * @author Philip Whitehouse
 *
 */
public class Connection {

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
	 * @param clientInfo
	 * @param nanoTime
	 * @param b
	 */
	public Connection(ClientInfo clientInfo, long nanoTime, boolean b) {
		this.clientInfo = clientInfo;
		this.lastConnectionTime = nanoTime;
		this.active = b;
	}

	/**
	 *
	 */
	void setActive() {
		active = true;
	}

	/**
	 *
	 * @param nanoTime
	 */
	void addNewConnnection(long nanoTime) {
		lastConnectionTime = nanoTime;
	}
	
	/**
	 * 
	 */
	public void connect() {
		lastConnectionTime = System.nanoTime();
		active = true;
	}

	/**
	 * 
	 */
	public void disconnect() {
		active = false;
	}

}
