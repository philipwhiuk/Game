package com.whiuk.philip.game.client;

import com.whiuk.philip.game.shared.Messages.ClientMessage;
import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 *
 * @author Philip Whitehouse
 *
 */
public class GameClient {
	/**
	 *
	 */
	private static final String HOST = "localhost";
	/**
	 *
	 */
	private static final int PORT = 8443;
	/**
	 * 
	 */
	private static GameClient gameClient;
	/**
	 *
	 */
	private final NetworkThread ntwThread;
	private boolean connected;
	private boolean running;

	/**
	 *
	 */
	public GameClient() {
		ntwThread = new NetworkThread(HOST, PORT);
	}

	/**
	 * Run game client.
	 */
	public final void run() {
		ntwThread.start();
		running = true;
		while(running) {
			while(!connected) {
				
			}
			while(connected) {

			}
		}
	}
	/**
	 * Process message.
	 * @param message
	 */
	public void processInboundMessage(final ServerMessage message) {
		// TODO Auto-generated method stub

	}

	/**
	 * Singleton access.
	 * @return client
	 */
	public static GameClient getGameClient() {
		return gameClient;
	}
	/**
	 * Singleton setter.
	 * @param client
	 * @return
	 */
	public static void setGameClient(final GameClient client) {
		gameClient = client;
	}

	public void setConnected(boolean b) {
		connected = b;
	}

}
