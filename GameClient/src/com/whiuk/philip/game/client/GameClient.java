package com.whiuk.philip.game.client;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.whiuk.philip.game.shared.Messages.ServerMessage;

/**
 *
 * @author Philip Whitehouse
 *
 */
public class GameClient {
	/**
	 * Network host.
	 */
	private static final String HOST = "localhost";
	/**
	 * Network port.
	 */
	private static final int PORT = 8443;
	/**
	 * Game client singleton.
	 */
	private static GameClient gameClient;
	/**
	 *
	 */
	private final NetworkThread ntwThread;
	/**
	 * Connection status.
	 */
	private volatile boolean connected;
	/**
	 * Class logger.
	 */
	private static final transient Logger LOGGER =
			Logger.getLogger(GameClient.class);
	/**
	 * Display width.
	 */
	private static final int DISPLAY_WIDTH = 800;
	/**
	 * Display hieght.
	 */
	private static final int DISPLAY_HEIGHT = 600;
	/**
	 * Title.
	 */
	private static final String GAME_CLIENT_TITLE = "The Game";

	/**
	 *
	 */
	public GameClient() {
    	//TODO: Initialize logging properly
    	BasicConfigurator.configure();
		ntwThread = new NetworkThread(HOST, PORT);
	}

	/**
	 * Run game client.
	 */
	public final void run() {
		ntwThread.start();
		try {
			Display.setDisplayMode(new DisplayMode(
					DISPLAY_WIDTH, DISPLAY_HEIGHT));
			Display.setTitle(GAME_CLIENT_TITLE);
			Display.create();
		} catch (LWJGLException e) {
			handleLWJGLException(e);
		}
		while (!Display.isCloseRequested()) {
			// render OpenGL here
			Display.update();
		}
		Display.destroy();
		closeNetworkConnection();
	}
	/**
	 * Handles a fatal LWJGL exception.
	 * @param e Exception
	 */
	private void handleLWJGLException(final LWJGLException e) {
		LOGGER.fatal("Failed to create display", e);
		closeNetworkConnection();
		System.exit(1);
	}

	/**
	 * Close the network connection.
	 */
	private void closeNetworkConnection() {
		ntwThread.disconnectAndStop();
		try {
			ntwThread.join();
		} catch (InterruptedException e1) {
			LOGGER.info("Interrupted while closing network thread.");
		}
	}

	/**
	 * Process message.
	 * @param message Server message
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
	 * @param client Client
	 */
	public static void setGameClient(final GameClient client) {
		gameClient = client;
	}

	/**
	 * Update connection status.
	 * @param b Connection status
	 */
	public final void setConnected(final boolean b) {
		connected = b;
	}

}
