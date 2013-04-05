package com.whiuk.philip.game.server.game;

import org.apache.log4j.Logger;

import com.whiuk.philip.util.watchdog.Watchable;
import com.whiuk.philip.util.watchdog.Watchdog;

/**
 * Game world
 * @author Philip Whitehouse
 *
 */
public class GameWorld extends Thread implements Watchable {
	/**
	 * Class logger.
	 */
	public static final Logger LOGGER = Logger.getLogger(GameWorld.class);
	/**
	 * Whether the world is running.
	 */
	private boolean running;
	/**
	 * Time a game world tick takes in milliseconds.
	 */
	private static final long TICK_TIME = 0;
	/**
	 * Watchdog monitoring the game world.
	 */
	private Watchdog watchdog;

	/**
	 * A single tick of the game world.
	 */
	public void tick() {

	}

	@Override
	public final void run() {
		long time;
		running = true;
		while (running) {
			time = System.nanoTime();
			tick();
			watchdog.pat();
			if (System.nanoTime() < time + TICK_TIME) {
				try {
					Thread.sleep(time + TICK_TIME - System.nanoTime());
				} catch (InterruptedException e) {
					LOGGER.trace("GameWorld Sleep Interrupted", e);
				}
			} else {
				LOGGER.info("Slow tick");
			}
		}
	}

	@Override
	public void warn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWatchdog(Watchdog watchdog) {
		this.watchdog = watchdog;
	}

	/**
	 * Loads the game world
	 * @return
	 */
	public static GameWorld load() {
		// TODO Auto-generated method stub
		return new GameWorld();
	}
}
