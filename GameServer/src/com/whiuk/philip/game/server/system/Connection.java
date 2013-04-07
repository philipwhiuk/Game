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
	 * @param c
	 * @param nanoTime
	 * @param b
	 */
	public Connection(final ClientInfo c, final long nanoTime, final boolean b) {
		this.clientInfo = c;
		this.lastConnectionTime = nanoTime;
		this.active = b;
	}

	/**
	 *
	 */
	final void setActive() {
		active = true;
	}

	/**
	 *
	 * @param nanoTime
	 */
	final void addNewConnnection(final long nanoTime) {
		lastConnectionTime = nanoTime;
	}

	/**
	 *
	 */
	public final void connect() {
		lastConnectionTime = System.nanoTime();
		active = true;
	}

	/**
	 *
	 */
	public final void disconnect() {
		active = false;
	}

}
